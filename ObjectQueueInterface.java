/**
 * Class interface for object queue data structure
 * Modified: Nirmal Karthikeyan
 * Github: github.com/cyberwizard1001/spamcode
 */

public interface ObjectQueueInterface{

    public boolean isEmpty();

    public boolean isFull();

    public void clear();

    public void insert(Object o);

    public Object extract();

    public Object query();
}