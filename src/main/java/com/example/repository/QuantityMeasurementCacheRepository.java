package com.example.repository;

import com.example.exception.*;
//import com.example.repository.*;
import com.example.entity.QuantityMeasurementEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementCacheRepository implements QuantityMeasurementRepository {

    private static QuantityMeasurementCacheRepository instance;
    private static final String DATA_FILE = "quantity_measurements.dat";

    private List<QuantityMeasurementEntity> cache = new ArrayList<>();

    private QuantityMeasurementCacheRepository() {
        loadFromDisk();
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) throws DatabaseException {
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() throws DatabaseException {
        return new ArrayList<>(cache);
    }

    /**
     * Saves a single entity to disk using serialization.
     * Uses AppendableObjectOutputStream to handle multiple objects in the file.
     *
     * @param entity the entity to save
     */
    private void saveToDisk(QuantityMeasurementEntity entity) {
        try {
            // Check if file exists to determine if this is append operation
            File file = new File(DATA_FILE);
            boolean fileExists = file.exists();

            if (fileExists) {
                // Append to existing file
                try (OutputStream fileOut = new FileOutputStream(file, true);
                     AppendableObjectOutputStream objOut = new AppendableObjectOutputStream(fileOut)) {
                    objOut.writeObject(entity);
                    objOut.flush();
                }
            } else {
                // Create new file
                try (ObjectOutputStream objOut = new ObjectOutputStream(
                        new FileOutputStream(file))) {
                    objOut.writeObject(entity);
                    objOut.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving entity to disk: " + e.getMessage());
        }
    }

    /**
     * Loads all entities from disk into the in-memory cache.
     * This method is called during initialization to restore previous state.
     */
    private void loadFromDisk() {
        cache.clear();
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return; // File doesn't exist yet, start with empty cache
        }

        try (ObjectInputStream objIn = new ObjectInputStream(
                new FileInputStream(file))) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity = 
                        (QuantityMeasurementEntity) objIn.readObject();
                    cache.add(entity);
                } catch (EOFException e) {
                    // End of file reached
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading entities from disk: " + e.getMessage());
        }
    }

	
}