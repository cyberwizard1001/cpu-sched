/**
 * Class representing the central processing unit to process jobs in turn
 * @author Andrews Samuel
 * @version 24/3/17
 */
    class CentralProcessingUnit{
    Job j;
    int quanta,quantum;
    boolean occupied;
    /**
     * Constructor for central processing unit
     */
    public CentralProcessingUnit(){
        j=null;
        quanta=0;//represents remaining quanta for job
        quantum=quanta;//represents starting quantum 2, 4, 8, or 16 in this case
        occupied=false;
    }
    /**
     * Central processing unit accepts a job for processing
     * @param job to be processed
     */
    public void takeJob(Job jobbie){
        j=jobbie;
        occupied=true;
    }
    /**
     * Processes the current job in central processing unit
     */
    public void process(){
        j.decrementTime();   //--timeremaining
        --quanta;
    }
    /**
     * Initializes quanta and quantum for the incoming job
     * @param time to the current quanta/quantum is to be set 2, 4, 8, or 16 in this case
     */
    public void setQuanta(int time){
        quanta=time;
        quantum=quanta;
    }
    /**
     * Determines whether or not the job still has time for processing in CPU
     * @return true if time is up, false otherwise
     */
    public boolean outOfTime(){
        return quanta==0;
    }
    /**
     * Returns quantum in which job was initialy set to
     * @return quantum number 2, 4, 8, or 16 in this case
     */
    public int getQuantum(){
        return quantum;
    }
    /**
     * Returns the current job in CPU
     * @return current job in CPU
     */
    public Job currentJob(){
        return j;
    }
    /**
     * Releases and returns job after CPU is done processing, and resets CPU stats
     * @return processed job
     */
    public Job releaseJob(){
        occupied=false;
        Job temp=j;
        j=null;
        return temp;
    }
    /**
     * Determines whether or not CPU is occupied and is available to take another job
     * @return true if CPU has a job in proccess, false if vacant
     */
    public boolean isOccupied(){
        return occupied;
    }
}