package com.example.repository;

import com.example.entity.QuantityMeasurementEntity;
import com.example.exception.DatabaseException;
import com.example.util.ConnectionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * JDBC-based database repository for persisting QuantityMeasurementEntity objects.
 * Uses connection pooling for efficient database access and parameterized queries
 * to prevent SQL injection attacks.
 */
public class QuantityMeasurementDatabaseRepository implements QuantityMeasurementRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);
    
    private final ConnectionPool connectionPool;
    private volatile boolean initialized = false;
    
    public QuantityMeasurementDatabaseRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        logger.info("QuantityMeasurementDatabaseRepository initialized");
    }
    
    /**
     * Initialize the repository by setting up the connection pool and creating schema.
     */
    public void initialize() throws DatabaseException {
        if (initialized) {
            logger.warn("Repository already initialized");
            return;
        }
        
        try {
            connectionPool.initialize();
            createSchema();
            initialized = true;
            logger.info("Database repository initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database repository", e);
            throw new DatabaseException("Repository initialization failed", e);
        }
    }
    
    /**
     * Create the database schema if it doesn't exist.
     */
    private void createSchema() throws DatabaseException {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS quantity_measurement_entity (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "operation VARCHAR(50) NOT NULL, " +
                    "operand1 VARCHAR(255), " +
                    "operand2 VARCHAR(255), " +
                    "result VARCHAR(255), " +
                    "error VARCHAR(500), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                    "INDEX idx_operation (operation), " +
                    "INDEX idx_created_at (created_at) " +
                    ")";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
                logger.info("Database schema created/verified successfully");
            }
        } catch (SQLException e) {
            logger.error("Failed to create database schema", e);
            throw new DatabaseException("Schema creation failed", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
    
    /**
     * Save a QuantityMeasurementEntity to the database.
     */
    @Override
    public void save(QuantityMeasurementEntity entity) throws DatabaseException {
        if (entity == null) {
            logger.warn("Attempting to save null entity");
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            
            String insertSQL = "INSERT INTO quantity_measurement_entity " +
                    "(operation, operand1, operand2, result, error) " +
                    "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, entity.getOperation());
                pstmt.setString(2, entity.getOperand1());
                pstmt.setString(3, entity.getOperand2());
                pstmt.setString(4, entity.getResult());
                pstmt.setString(5, entity.getError());
                
                pstmt.executeUpdate();
                logger.debug("Entity saved with operation: {}", entity.getOperation());
            }
        } catch (SQLException e) {
            logger.error("Failed to save entity with operation: {}", entity.getOperation(), e);
            throw new DatabaseException("Failed to save entity", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
    
    /**
     * Retrieve all QuantityMeasurementEntity objects from the database.
     */
    @Override
    public List<QuantityMeasurementEntity> findAll() throws DatabaseException {
        List<QuantityMeasurementEntity> entities = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = connectionPool.getConnection();
            String selectSQL = "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {
                while (rs.next()) {
                    entities.add(mapResultSetToEntity(rs));
                }
                logger.debug("Retrieved {} entities from database", entities.size());
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve entities from database", e);
            throw new DatabaseException("Failed to retrieve entities", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        
        return entities;
    }
    
    /**
     * Get measurement count from database.
     */
    public long getTotalCount() throws DatabaseException {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            String countSQL = "SELECT COUNT(*) FROM quantity_measurement_entity";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    long count = rs.getLong(1);
                    logger.debug("Total measurement count: {}", count);
                    return count;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to count entities", e);
            throw new DatabaseException("Failed to count entities", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        
        return 0;
    }
    
    /**
     * Delete all measurements from the database.
     */
    public void deleteAll() throws DatabaseException {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            String deleteSQL = "DELETE FROM quantity_measurement_entity";
            
            try (Statement stmt = conn.createStatement()) {
                int rowsDeleted = stmt.executeUpdate(deleteSQL);
                logger.info("Deleted {} measurements from database", rowsDeleted);
            }
        } catch (SQLException e) {
            logger.error("Failed to delete all measurements", e);
            throw new DatabaseException("Failed to delete measurements", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
    
    /**
     * Get connection pool statistics.
     */
    public String getPoolStatistics() {
        return connectionPool.getPoolStatistics();
    }
    
    /**
     * Release database resources and close connections.
     */
    public void releaseResources() {
        logger.info("Releasing database repository resources");
        connectionPool.closeAll();
    }
    
    /**
     * Map a ResultSet row to a QuantityMeasurementEntity object.
     */
    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        String operation = rs.getString("operation");
        String operand1 = rs.getString("operand1");
        String operand2 = rs.getString("operand2");
        String result = rs.getString("result");
        String error = rs.getString("error");
        
        if (error != null) {
            return new QuantityMeasurementEntity(operation, error);
        }
        return new QuantityMeasurementEntity(operation, operand1, operand2, result);
    }
}