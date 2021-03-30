/**
* Class implementation for the queue data structure
 * Modified by Nirmal Karthikeyan
 * Github: github.com/cybwerwizard1001/spamcode
 */

public class ObjectQueue implements ObjectQueueInterface{
    private Object[] item;
    private int front,rear,count;

    //Constructor
    public ObjectQueue() {
        item=new Object[1];
        front=0;
        rear=-1;
        count=0;
    }

    //Returns true if queue is empty
    public boolean isEmpty() {
        return count==0;
    }

    //Returns true if queue is full
    public boolean isFull() {
        return count==item.length;
    }

    //Clears queue
    public void clear() {
        item=new Object[1];
        front=0;
        rear=-1;
        count=0;
    }


    public void insert(Object obj) {
        if (isFull())
            resize(2*item.length);
        rear=(rear+1)%item.length;
        item[rear]=obj;
        ++count;
        //System.out.print(((Job)obj).getPid()+"\n");
    }

    //REMOVE and return element
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

    //RETURN element
    public Object query() {
        if (isEmpty()) {
            System.out.print("Queue Underflow\n");
            new Exception("Query Runtime Error: Queue Underflow").printStackTrace();
            System.exit(1);
        }
        return item[front];
    }


     //Resizes the array in which queue is represented. Used for insertion and removal
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