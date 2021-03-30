import java.io.*;
import java.util.Scanner;
/**
 * Class to simulate a multi level feedback queue in which Process are scheduled for cpu processing
 * Modified: Nirmal Karthikeyan
 * Github: github.com/cyberwizard1001/spamcode
 */


public class MultiLevelFeedbackQueue {
    static final int QUANTUM1 = 2, QUANTUM2 = 4, QUANTUM3 = 8, QUANTUM4 = 16, QUANTUM5 = 32, QUANTUM6 = 64, QUANTUM7 = 128, QUANTUM8 = 256;
    int clock, numberOfProcess, sumTotalTime, sumResponseTime, totalSize;
    CentralProcessingUnit cpu;
    ObjectQueue entrance, q1, q2, q3, q4,q5,q6,q7,q8;


    //Constructor for multi level feedback queue

    public MultiLevelFeedbackQueue() {
        clock = 0;
        numberOfProcess = 0;
        sumTotalTime = 0;
        totalSize = 0;
        //declaring cpu object
        cpu = new CentralProcessingUnit();
        //declaring queue to hold processes for execution
        entrance = new ObjectQueue();
        //declaring actual queues
        q1 = new ObjectQueue();
        q2 = new ObjectQueue();
        q3 = new ObjectQueue();
        q4 = new ObjectQueue();
        q5 = new ObjectQueue();
        q6 = new ObjectQueue();
        q7 = new ObjectQueue();
        q8 = new ObjectQueue();

    }


    // Function to get process data from user
    public void retrieveProcess() {
        int[] proc_data = new int[3];
        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of processes: ");
        int num_proc;

        num_proc = input.nextInt();

        System.out.println("Arrival time\tPID\tBurst time");
        for(int i=0;i<num_proc;i++)
        {

        proc_data[0] = input.nextInt();
        proc_data[1] = input.nextInt();
        proc_data[2] = input.nextInt();

        entrance.insert(new Job(proc_data[0], proc_data[1], proc_data[2]));
        }

    }


    //Print a header to display output
    public void outputHeader(){
        System.out.format("Event%8s%5s%5s%8s%10s\n","Clock","PID","Size","Elapsed","End Queue");
    }

    //Simulation of multilevel feedback queue with round robin scheduling
    public void simulate(){
        //Checking if process store is empty (to ensure that empty queue is not read)
        while(!entrance.isEmpty()){
            //inserting to first queue when there are no other processes in the queues.
            if(!cpu.isOccupied()&&q1.isEmpty()&&q2.isEmpty()&&q3.isEmpty()&&q4.isEmpty())
                clock=((Job)entrance.query()).getArrivalTime();
            if(clock==((Job)entrance.query()).getArrivalTime())
                //function that prints process details, increments size and adds process to queue 1
                enterSystem();
            if(!cpu.isOccupied())
                //(when there is a process in the queues, the highest priority one is to be submitted for exec
                submitJob();
            //cpu execution for a cycle
            cpu.process();
            ++clock;
            //if current job is over, it has to be released so CPU state can be marked Free
            if(cpu.currentJob().isFinished()){
                sumTotalTime+=clock-cpu.currentJob().getArrivalTime();
                departureMessage(cpu.releaseJob());
            }
            //'process is not over but time quantum has run out
            else if(cpu.outOfTime())
                //give up control of CPU to next allocated process
                relinquishJob();
        }
        //CPU is not occupied but one of the queues is not empty
        while(!(!cpu.isOccupied()&&q1.isEmpty()&&q2.isEmpty()&&q3.isEmpty()&&q4.isEmpty())){
            ++clock;
            if(!cpu.isOccupied())
                //submit job with highest priority for execution
                submitJob();
            //execute the process
            cpu.process();
            //Again if process is over then give control to next process
            if(cpu.currentJob().isFinished()){
                sumTotalTime+=clock-cpu.currentJob().getArrivalTime();
                departureMessage(cpu.releaseJob());
            }
            //If quanta has been exceeded then control is given to next process.
            else if(cpu.outOfTime())
                relinquishJob();
        }        
    }

    //Function to release the job and insert the incomplete job to the next highest priority queue
    private void relinquishJob(){
        switch (cpu.getQuantum()) {
            case 2 -> q2.insert(cpu.releaseJob());
            case 4 -> q3.insert(cpu.releaseJob());
            case 8 -> q4.insert(cpu.releaseJob());
            case 16 -> q5.insert(cpu.releaseJob());
            case 32 -> q6.insert(cpu.releaseJob());
            case 64 -> q7.insert(cpu.releaseJob());
            case 128,256 -> q8.insert(cpu.releaseJob());
        }
    }

    //Assign first job from Entrance queue from queue 1 for execution
    private void enterSystem(){
        arrivalMessage((Job)entrance.query());
        totalSize+=((Job)entrance.query()).getSize();
        q1.insert((Job)entrance.extract());
        ++numberOfProcess;
    }

    //Function to decide on the next most important thread, using thread complexity
    private void submitJob(){
        if(!q1.isEmpty()){
            //Assign job for execution, set quantum and quanta to QUANTUMX
            cpu.takeJob((Job)q1.extract());
            cpu.setQuanta(QUANTUM1);               //copied to qyantum
            sumResponseTime+=clock-cpu.currentJob().getArrivalTime();
        }
        else if(!q2.isEmpty()){
            //If queue 1 is empty, insertion happens from queue 2
            cpu.takeJob((Job)q2.extract());
            cpu.setQuanta(QUANTUM2);
        }
        else if(!q3.isEmpty()){
            //If queue 2 is empty, insertion happens from queue 3
            cpu.takeJob((Job)q3.extract());
            cpu.setQuanta(QUANTUM3);
        }
        else if(!q4.isEmpty()){
            //If queue 3 is empty, insertion happens from queue 4
            cpu.takeJob((Job)q4.extract());
            cpu.setQuanta(QUANTUM4);            
        }

        else if(!q5.isEmpty()){
            //If queue 4 is empty, insertion happens from queue 5
            cpu.takeJob((Job)q5.extract());
            cpu.setQuanta(QUANTUM5);
        }

        else if(!q6.isEmpty()){
            //If queue 5 is empty, insertion happens from queue 6
            cpu.takeJob((Job)q6.extract());
            cpu.setQuanta(QUANTUM6);
        }

        else if(!q7.isEmpty()){
            //If queue 7 is empty, insertion happens from queue 8
            cpu.takeJob((Job)q7.extract());
            cpu.setQuanta(QUANTUM7);
        }

        else if(!q8.isEmpty()){
            //If queue 7 is empty, insertion happens from queue 8
            cpu.takeJob((Job)q8.extract());
            cpu.setQuanta(QUANTUM8);
        }

    }

    //Printing output
    public void outputStats(){
        System.out.println("Process Completed:" + numberOfProcess);
        System.out.println("Sum Process Time: %d" + sumTotalTime);
        System.out.println("Total CPU Idle Time: " + (clock-totalSize));
        System.out.println("Average Response Time: "+((float)sumResponseTime/numberOfProcess));
        System.out.println("Average Turnarround: " + ((float)sumTotalTime/numberOfProcess));
        System.out.println("Average Wait Time: " + (((float)sumTotalTime-totalSize)/numberOfProcess));
        System.out.println("Average Throughput: "+((float)numberOfProcess/sumTotalTime));
    }

    //Print details when process arrives into execution
    private void arrivalMessage(Job jo){
        System.out.format("Arrival%6d%5d%5d\n",clock,jo.getPid(),jo.getTimeRemaining());
    }

    //Print details when process exits after completion
    private void departureMessage(Job jo){
        System.out.format("Departure%4d%5d%13d",clock,jo.getPid(),(clock-jo.getArrivalTime()));
        if(jo.getSize()<=2)
            System.out.format("%10d\n",1);
        else if(jo.getSize()<=6)
            System.out.format("%10d\n",2);
        else if(jo.getSize()<=14)
            System.out.format("%10d\n",3);
        else
            System.out.format("%10d\n",4);

    }
}