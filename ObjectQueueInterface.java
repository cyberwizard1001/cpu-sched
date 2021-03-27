/**
 * Class interface for object queue data structure
 * @author Andrews Samuel
 * @version 24/3/17
 */
public interface ObjectQueueInterface{
    /**
     * Determines whether or not queue is empty
     * @return true if empty, false if it contains object(s)
     */
    public boolean isEmpty();
    /**
     * Determines whether or not queue is full
     * @return true if full, false if it still has room for more objects
     */
    public boolean isFull();
    /**
     * Clears queue by creating a new location in memory for a new one
     */
    public void clear();
    /**
     * Places an object into rear of queue
     * @param object o to be inserted into queue
     */
    public void insert(Object o);
    /**
     * Removes an object from the front of queue
     * @return the object removed from front of queue
     */
    public Object extract();
    /**
     * Returns the object in front of queue without removing it
     * @return object in front of queue
     */
    public Object query();
}