package alterego.list;

import alterego.data.Storable;

/**
 * Represents a generic data list that can hold items of type T.
 * Provides common operations for managing collections of storable objects
 * @param <T> The type of items stored in this list, must implement Storable
 */
public interface DataList <T extends Storable> {
    /**
     * Returns the load status message from initialization.
     * @return Status message if file had issues, null if loaded successfully
     */
    public String getLoadStatus();

    /**
     * Appends a load status message to the existing status.
     * @param loadStatus The status message to add
     */
    public void addLoadStatus(String loadStatus);

    /**
     * Adds an item to the list.
     * Only used for storage loading.
     * @param item The item to be added
     */
    public void addItem(T item);
}
