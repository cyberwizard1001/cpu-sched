import java.io.*;
/**
 * Driver
 * Modified: Nirmal Karthikeyan
 * Github: github.com/cyberwizard1001/spamcode
 */
public class Driver{

    //Main method for multi level feedback queue

    public static void main(String[] args){
        MultiLevelFeedbackQueue mfq=new MultiLevelFeedbackQueue();
        mfq.retrieveProcess();
        mfq.outputHeader();
        mfq.simulate();
        mfq.outputStats();
    }
}