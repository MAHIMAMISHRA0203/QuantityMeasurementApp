package com.example.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe connection pool for managing JDBC database connections.
 * Implements a simple queue-based pool with configurable min/max sizes,
 * connection validation, and timeout handling.
 */
public class ConnectionPool {
    
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private static final ReentrantLock instanceLock = new ReentrantLock();
    private static final ReentrantLock poolLock = new ReentrantLock();
    
    private Queue<Connection> availableConnections;
    private Set<Connection> usedConnections;
    private int minSize;
    private int maxSize;
    private long connectionTimeoutMs;
    private String dbDriver;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private boolean initialized;
    
    private ConnectionPool() {
        initialized = false;
    }
    
    /**
     * Get singleton instance of ConnectionPool.
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            instanceLock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            } finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Initialize the connection pool with configuration from ApplicationConfig.
     * Creates initial connections up to minSize.
     */
    public void initialize() {
        poolLock.lock();
        try {
            if (initialized) {
                logger.warn("Connection pool already initialized");
                return;
            }
            
            ApplicationConfig config = ApplicationConfig.getInstance();
            this.minSize = config.getMinPoolSize();
            this.maxSize = config.getMaxPoolSize();
            this.connectionTimeoutMs = config.getConnectionTimeoutMs();
            this.dbDriver = config.getDbDriver();
            this.dbUrl = config.getDbUrl();
            this.dbUsername = config.getDbUsername();
            this.dbPassword = config.getDbPassword();
            
            this.availableConnections = new ArrayDeque<>();
            this.usedConnections = Collections.synchronizedSet(new HashSet<>());
            
            // Load database driver
            try {
                Class.forName(dbDriver);
                logger.info("Database driver loaded: {}", dbDriver);
            } catch (ClassNotFoundException e) {
                logger.error("Failed to load database driver {}: {}", dbDriver, e.getMessage());
                throw new RuntimeException("Database driver not found: " + dbDriver, e);
            }
            
            // Create initial connections
            for (int i = 0; i < minSize; i++) {
                Connection conn = createConnection();
                if (conn != null) {
                    availableConnections.offer(conn);
                }
            }
            
            initialized = true;
            logger.info("Connection pool initialized with min={}, max={}, driver={}", minSize, maxSize, dbDriver);
        } finally {
            poolLock.unlock();
        }
    }
    
    /**
     * Create a new database connection.
     */
    private Connection createConnection() {
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            logger.debug("New connection created: {}", conn);
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to create connection to {}: {}", dbUrl, e.getMessage());
            return null;
        }
    }
    
    /**
     * Get a connection from the pool with retry and timeout logic.
     */
    public Connection getConnection() throws SQLException {
        poolLock.lock();
        try {
            if (!initialized) {
                throw new SQLException("Connection pool not initialized");
            }
            
            long startTime = System.currentTimeMillis();
            Connection conn = null;
            
            while (System.currentTimeMillis() - startTime < connectionTimeoutMs) {
                // Try to get available connection
                conn = availableConnections.poll();
                
                if (conn != null) {
                    // Validate connection before returning
                    if (isConnectionValid(conn)) {
                        usedConnections.add(conn);
                        logger.debug("Reusing connection from pool. Available: {}, InUse: {}", 
                                availableConnections.size(), usedConnections.size());
                        return conn;
                    } else {
                        // Invalid connection, discard it
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            logger.debug("Error closing invalid connection", e);
                        }
                    }
                }
                
                // Create new connection if pool not at max
                if (usedConnections.size() + availableConnections.size() < maxSize) {
                    conn = createConnection();
                    if (conn != null) {
                        usedConnections.add(conn);
                        logger.debug("New connection created from pool. Available: {}, InUse: {}", 
                                availableConnections.size(), usedConnections.size());
                        return conn;
                    }
                }
                
                // Wait before retry
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Connection retrieval interrupted", e);
                }
            }
            
            throw new SQLException(String.format(
                    "Unable to get connection from pool after %d ms. Pool exhausted.", connectionTimeoutMs));
        } finally {
            poolLock.unlock();
        }
    }
    
    /**
     * Release a connection back to the pool.
     */
    public void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        
        poolLock.lock();
        try {
            if (usedConnections.remove(conn)) {
                if (isConnectionValid(conn)) {
                    availableConnections.offer(conn);
                    logger.debug("Connection returned to pool. Available: {}, InUse: {}", 
                            availableConnections.size(), usedConnections.size());
                } else {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        logger.debug("Error closing invalid connection", e);
                    }
                }
            }
        } finally {
            poolLock.unlock();
        }
    }
    
    /**
     * Check if a connection is still valid.
     */
    private boolean isConnectionValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            logger.debug("Connection validation failed", e);
            return false;
        }
    }
    
    /**
     * Get connection pool statistics.
     */
    public String getPoolStatistics() {
        poolLock.lock();
        try {
            int available = availableConnections.size();
            int inUse = usedConnections.size();
            int total = available + inUse;
            return String.format("ConnectionPool Statistics: Available=%d, InUse=%d, Total=%d, MaxSize=%d", 
                    available, inUse, total, maxSize);
        } finally {
            poolLock.unlock();
        }
    }
    
    /**
     * Close all connections and cleanup the pool.
     */
    public void closeAll() {
        poolLock.lock();
        try {
            // Close available connections
            while (!availableConnections.isEmpty()) {
                Connection conn = availableConnections.poll();
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    logger.debug("Error closing available connection", e);
                }
            }
            
            // Close used connections
            for (Connection conn : usedConnections) {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    logger.debug("Error closing used connection", e);
                }
            }
            
            usedConnections.clear();
            initialized = false;
            logger.info("All connections closed. Connection pool cleaned up.");
        } finally {
            poolLock.unlock();
        }
    }
    
    /**
     * Reset the pool instance (useful for testing).
     */
    public static void resetInstance() {
        instanceLock.lock();
        try {
            if (instance != null) {
                instance.closeAll();
                instance = null;
            }
        } finally {
            instanceLock.unlock();
        }
    }
}