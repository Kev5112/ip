package alterego.data;

/**
 * Represents an object that can be converted to a file storage format.
 * Implemented by classes that need to be saved to or loaded from persistent storage.
 */
public interface Storable {
    /**
     * Returns a string representation of this object formatted for file storage.
     * @return formatted string for file storage
     */
    public String toFileFormat();
}
