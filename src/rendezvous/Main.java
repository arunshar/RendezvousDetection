package rendezvous;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        DataReading myDataReading = new DataReading();
        myDataReading.readMissingSegments(); // read in those missing segments, sort them

        // run the baseline plane sweep algorithm
        BaselinePlaneSweepApproach myBaselinePlaneSweepApproach = new BaselinePlaneSweepApproach();
        myBaselinePlaneSweepApproach.planeSweep();
    }
}