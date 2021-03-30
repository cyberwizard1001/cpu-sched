import java.io.*;
/**
 * Driver for multi level feedback queue simulation
 * @author Andrews Samuel
 * @version 24/3/17
 */
public class Driver{
    /**
     * Main method for multi level feedback queue
     */
    public static void main(String[] args)throws IOException{
        //Andrews Samuel
        //008559913
        PrintWriter pw=new PrintWriter(new FileWriter("csci.txt"));
        MultiLevelFeedbackQueue mfq=new MultiLevelFeedbackQueue();
        mfq.retrieveProcess();
        mfq.outputHeader();
        mfq.simulate();
        mfq.outputStats();
        pw.close();
    }
}