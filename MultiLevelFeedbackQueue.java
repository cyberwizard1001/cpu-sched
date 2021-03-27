import java.io.*;
import java.util.Scanner;
/**
 * Class to simulate a multi level feedback queue in which jobs are scheduled for cpu processing
 * @author Andrews Samuel
 * @version 24/3/17
 */
public class MultiLevelFeedbackQueue {
    static final int QUANTUM1 = 2, QUANTUM2 = 4, QUANTUM3 = 8, QUANTUM4 = 16;
    Scanner in;
    PrintWriter pw;
    int clock, numberOfJobs, sumTotalTime, sumResponseTime, totalSize;
    CentralProcessingUnit cpu;
    ObjectQueue enterance, q1, q2, q3, q4;

    /**
     * Constructor for multi level feedback queue
     *
     * @param pw print writer object for output destination
     */
    public MultiLevelFeedbackQueue(PrintWriter pewie) throws IOException {
        in = new Scanner(new File("mfq.txt"));
        pw = pewie;
        clock = 0;
        numberOfJobs = 0;
        sumTotalTime = 0;
        sumResponseTime = 0;
        totalSize = 0;
        cpu = new CentralProcessingUnit();
        enterance = new ObjectQueue();
        q1 = new ObjectQueue();
        q2 = new ObjectQueue();
        q3 = new ObjectQueue();
        q4 = new ObjectQueue();
    }

    /**
     * Takes specification from input file, converts them into jobs, and places them into enterance queue accordingly
     */
    public void retrieveJobs() {
        int jobSpec[] = new int[3];
        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of processes: ");
        int num_proc;

        num_proc = input.nextInt();

        System.out.println("Arrival time\tPID\tBurst time");
        for(int i=0;i<num_proc;i++)
        {

        jobSpec[0] = input.nextInt();
        jobSpec[1] = input.nextInt();
        jobSpec[2] = input.nextInt();

        enterance.insert(new Job(jobSpec[0], jobSpec[1], jobSpec[2]));
        }

    }

    /**
     * Outputs header for output file to display jobs as they enter and leave the system
     */

    public void outputHeader(){
        System.out.format("Event%8s%5s%5s%8s%10s\n","Clock","PID","Size","Elapsed","End Queue");
        pw.format("Event%8s%5s%5s%8s%10s\n","Clock","PID","Size","Elapsed","End Queue");
    }
    /**
     * Processes each job according through the multi level feedback queue until all jobs are completed
     */
    public void simulate(){
        while(!enterance.isEmpty()){
            if(!cpu.isOccupied()&&q1.isEmpty()&&q2.isEmpty()&&q3.isEmpty()&&q4.isEmpty())
                clock=((Job)enterance.query()).getArrivalTime();
            if(clock==((Job)enterance.query()).getArrivalTime())
                enterSystem();
            if(!cpu.isOccupied())
                submitJob();
            cpu.process();
            ++clock;
            if(cpu.currentJob().isFinished()){
                sumTotalTime+=clock-cpu.currentJob().getArrivalTime();
                departureMessage(cpu.releaseJob());
            }
            else if(cpu.outOfTime())
                relinquishJob();
        }
        while(!(!cpu.isOccupied()&&q1.isEmpty()&&q2.isEmpty()&&q3.isEmpty()&&q4.isEmpty())){
            ++clock;
            if(!cpu.isOccupied())
                submitJob();
            cpu.process();
            if(cpu.currentJob().isFinished()){
                sumTotalTime+=clock-cpu.currentJob().getArrivalTime();
                departureMessage(cpu.releaseJob());
            }
            else if(cpu.outOfTime())
                relinquishJob();
        }        
    }
    /**
     * Helper method which determines where each job is to be placed next after processing quantum runs out
     */
    private void relinquishJob(){
        switch(cpu.getQuantum()){
            case 2:q2.insert(cpu.releaseJob());break;
            case 4:q3.insert(cpu.releaseJob());break;
            case 8:case 16:q4.insert(cpu.releaseJob());break;
        }
    }
    /**
     * Helper method that places job from enterance queue into system
     */
    private void enterSystem(){
        arrivalMessage((Job)enterance.query());
        totalSize+=((Job)enterance.query()).getSize();
        q1.insert((Job)enterance.extract());
        ++numberOfJobs;
    }
    /**
     * Helper method that determines which queue the cpu is to draw from next
     */
    private void submitJob(){
        if(!q1.isEmpty()){
            cpu.takeJob((Job)q1.extract());
            cpu.setQuanta(QUANTUM1);
            sumResponseTime+=clock-cpu.currentJob().getArrivalTime();
        }
        else if(!q2.isEmpty()){
            cpu.takeJob((Job)q2.extract());
            cpu.setQuanta(QUANTUM2);
        }
        else if(!q3.isEmpty()){
            cpu.takeJob((Job)q3.extract());
            cpu.setQuanta(QUANTUM3);
        }
        else if(!q4.isEmpty()){
            cpu.takeJob((Job)q4.extract());
            cpu.setQuanta(QUANTUM4);            
        }
    }
    /**
     * Outputs a review of simulation
     */
    public void outputStats(){
        System.out.format("\n%s: %d\n%s: %d\n%s: %d\n%s: %.2f\n%s: %.2f\n%s: %.2f\n%s: %.2f\n",
                          "Jobs Completed: ",numberOfJobs,
                          "Sum Process Time: ",sumTotalTime,
                          "Total CPU Idle Time: ",(clock-totalSize),
                          "Average Response Time: ",((float)sumResponseTime/numberOfJobs),
                          "Average Turnarround: ",((float)sumTotalTime/numberOfJobs),
                          "Average Wait Time: ",(((float)sumTotalTime-totalSize)/numberOfJobs),
                          "Average Throughput: ",((float)numberOfJobs/sumTotalTime));
        pw.format("\n%s: %d\n%s: %d\n%s: %d\n%s: %.2f\n%s: %.2f\n%s: %.2f\n%s: %.2f\n",
                  "Jobs Completed: ",numberOfJobs,
                  "Sum Process Time: ",sumTotalTime,
                  "Total CPU Idle Time: ",(clock-totalSize),
                  "Average Response Time: ",((float)sumResponseTime/numberOfJobs),
                  "Average Turnarround: ",((float)sumTotalTime/numberOfJobs),
                  "Average Wait Time: ",(((float)sumTotalTime-totalSize)/numberOfJobs),
                  "Average Throughput: ",((float)numberOfJobs/sumTotalTime));
    }
    /**
     * Helper method to putput info of each job as it enters the system from enterance queue
     */
    private void arrivalMessage(Job jo){
        System.out.format("Arrival%6d%5d%5d\n",clock,jo.getPid(),jo.getTimeRemaining());
        pw.format("Arrival%6d%5d%5d\n",clock,jo.getPid(),jo.getTimeRemaining());
    }
    /**
     * Helper method to output info of each job as it leaves the system after having been fully processed
     */
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
        pw.format("Departure%4d%5d%13d",clock,jo.getPid(),(clock-jo.getArrivalTime()));
        if(jo.getSize()<=2)
            pw.format("%10d\n",1);
        else if(jo.getSize()<=6)
            pw.format("%10d\n",2);
        else if(jo.getSize()<=14)
            pw.format("%10d\n",3);
        else
            pw.format("%10d\n",4);
    }
}