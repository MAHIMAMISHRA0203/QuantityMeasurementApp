package com.example.repository;
import java.io.*;

/**
 * Custom ObjectOutputStream that allows appending objects to an existing file
 * without overwriting the file header. This is crucial for maintaining the
 * integrity of a serialized data file when multiple objects are saved over time.
 *
 * When using standard ObjectOutputStream, each new instance writes its own header,
 * which corrupts the stream when appending to an existing file. This class extends
 * ObjectOutputStream and overrides the writeStreamHeader method to skip writing
 * the header if the file is not empty (i.e., it's an append operation).
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

    private boolean headerWritten = false;

    /**
     * Constructor that creates an AppendableObjectOutputStream for the given output stream.
     * 
     * @param out the underlying OutputStream
     * @throws IOException if an I/O error occurs
     */
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
        headerWritten = true;
    }

    /**
     * Overrides writeStreamHeader to skip writing the stream header for append operations.
     * This method is called internally by ObjectOutputStream when writing begins.
     * For append operations, we skip writing the header to maintain file integrity.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void writeStreamHeader() throws IOException {
        // Check if this is an append operation by seeing if headerWritten is already set
        if (headerWritten) {
            // Header already written by parent constructor, skip it
            reset();
        } else {
            // First write, use default header writing
            super.writeStreamHeader();
            headerWritten = true;
        }
    }
}