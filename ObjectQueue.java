/**
 * Class interface for object queue data structure
 * @author Andrews Samuel
 * @version 24/3/17
 */
public class ObjectQueue implements ObjectQueueInterface{
    private Object[] item;
    private int front,rear,count;
    /**
     * Constructor for object queue
     */
    public ObjectQueue() {
        item=new Object[1];
        front=0;
        rear=-1;
        count=0;
    }
    /**
     * Determines whether or not queue is empty
     * @return true if empty, false if it contains object(s)
     */
    public boolean isEmpty() {
        return count==0;
    }
    /**
     * Determines whether or not queue is full
     * @return true if full, false if it still has room for more objects
     */
    public boolean isFull() {
        return count==item.length;
    }
    /**
     * Clears queue by creating a new location in memory for a new one
     */
    public void clear() {
        item=new Object[1];
        front=0;
        rear=-1;
        count=0;
    }
    /**
     * Places an object into rear of queue
     * @param object o to be inserted into queue
     */
    public void insert(Object owie) {
        if (isFull())
            resize(2*item.length);
        rear=(rear+1)%item.length;
        item[rear]=owie;
        ++count;
        //System.out.print(((Job)owie).getPid()+"\n");
    }
    /**
     * Removes an object from the front of queue
     * @return the object removed from front of queue
     */
    public Object extract() {
        if (isEmpty()) {
            System.out.print("Queue Underflow\n");
            new Exception("Extract Runtime Error: Queue Underflow").printStackTrace();
            System.exit(1);
        }
        Object temp=item[front];
        item[front]=null;
        front=(front+1)%item.length;
        --count;
        if (count==item.length/4&&item.length!=1)
            resize(item.length/2);
        return temp;
    }
    /**
     * Returns the object in front of queue without removing it
     * @return object in front of queue
     */
    public Object query() {
        if (isEmpty()) {
            System.out.print("Queue Underflow\n");
            new Exception("Query Runtime Error: Queue Underflow").printStackTrace();
            System.exit(1);
        }
        return item[front];
    }
    /**
     * Resizes the array in which queue is represented by creating an entirely new storage array and copying objects
     * @param size of new queue
     */
    private void resize(int size) {
        Object[] temp=new Object[size];
        for (int i=0;i<count;++i) {
            temp[i]=item[front];
            front=(front+1)%item.length;
        }
        front=0;
        rear=count-1;
        item=temp;
    }
}