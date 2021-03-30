/**
 * Class representing the central processing unit to process jobs in turn
 * Modified by: Nirmal Karthikeyan
 * Github: github.com/cyberwizard1001/spamcode
 */

    class CentralProcessingUnit{
    Job j;
    int quanta,quantum;
    boolean occupied;


    //Constructor
    public CentralProcessingUnit(){
        j=null;
        quanta=0; //represents remaining quanta for job
        quantum=quanta; //represents starting quantum
        occupied=false;  //used to identify if CPU is free or occupied
    }

    //Accept process for processing
    public void takeJob(Job proc){
        j=proc;
        //CPU busy state is set to true
        occupied=true;
    }

    //Actual CPU functioning (processing)
    public void process(){
        j.decrementTime();   // does --timeremaining for job 'process'
        --quanta;
    }

    //Givr value for quanta and quantum
    public void setQuanta(int time){
        quanta=time;
        quantum=quanta;
    }

    //If the particular quantum has been completed returns zero
    public boolean outOfTime(){
        return quanta==0;
    }

    //Returns quantum of current process
    public int getQuantum(){
        //2,4,8,16,32,64,128,256
        return quantum;
    }

//Returns the executing process
   public Job currentJob(){
        return j;
    }

    //Return completed job, reset CPU state
    public Job releaseJob(){
        occupied=false;
        Job temp=j;
        j=null;
        return temp;
    }

    //Return true if CPU is occupied, else false
    public boolean isOccupied(){
        return occupied;
    }
}