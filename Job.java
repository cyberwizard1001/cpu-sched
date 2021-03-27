/**
 * Class of a computing job that requires processing from cpu
 * @author Andrews Samuel
 * @version 24/3/17
 */
public class Job{
    private int pid,arrivalTime,size,timeRemaining,queueNum;
    /**
     * Constructor for Job class
     * @param at sets the time the job is to arrive in system
     * @param pidArg sets the identification number of job
     * @param s sets the initial size of job in quanta
     */
    public Job(int at,int pidArg,int s){
        pid=pidArg;
        arrivalTime=at;
        size=s;
        timeRemaining=size;
        queueNum=1;
    }
    /**
     * Returns the process identification number
     * @return pid process identification number
     */
    public int getPid(){
        return pid;
    }
    /**
     * Returns time job arrives in system
     * @return job arrival time
     */
    public int getArrivalTime(){
        return arrivalTime;
    }
    /**
     * Returns the size in quanta of job
     * @return job size in quanta
     */
    public int getSize(){
        return size;
    }
    /**
     * Returns job's remaining processing time required in quanta
     * @return remaining processing time in quanta
     */
    public int getTimeRemaining(){
        return timeRemaining;
    }
    /**
     * Returns the location of object in system
     * @return current or last queue number of job
     */
    public int getQueueNum(){
        return queueNum;
    }
    /**
     * Decreases remaining processing quanta of job by one
     */
    public void decrementTime(){
        --timeRemaining;
    }
    /**
     * Determines whether or not the job is complete
     */
    public boolean isFinished(){
        return timeRemaining==0;
    }
}