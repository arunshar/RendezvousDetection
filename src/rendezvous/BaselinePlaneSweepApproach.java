package rendezvous;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class BaselinePlaneSweepApproach {
    ArrayList<BeadIntersection> resultCRs; // an arrayList stores the result CR.
    LinkedList<BeadIntersection> msObservedLinkedList;  // a doubly-linked list to store the "observed object list"
    HashMap<MissingSegment, ArrayList<BeadIntersection>> msToBeadIntersectionMap; //  key = MissingSegment, value = a arraylist of bead intersections

    public void planeSweep() throws IOException{

        resultCRs = new ArrayList<>();
        msObservedLinkedList = new LinkedList<>();
        msToBeadIntersectionMap = new HashMap<>();
        BeadIntersection myBI = new BeadIntersection();

        double minLon = 10000000;
        double minLat = 10000000;
        double maxLon = -10000000;
        double maxLat = -10000000;


        long startTime = System.nanoTime();

        int countPass = 0;
        int countFail = 0;

        for (int i = 0; i < 1000; i++) {
            //for (int i = 0; i < DataReading.missingSegmentEndPointList.size(); i++) {

            System.out.println(i);
            // a vertical line sweeps from left to right, # of stops = # of end points
            //here the data is already sorted based on their longitude
            MissingSegmentEndPoint msEndPoint = DataReading.missingSegmentEndPointList.get(i);

            // this block computes the range of segments
            if (msEndPoint.lonX < minLon) {
                minLon = msEndPoint.lonX;
            }
            if (msEndPoint.lonX > maxLon) {
                maxLon = msEndPoint.lonX;
            }
            if (msEndPoint.latY < minLat) {
                minLat = msEndPoint.latY;
            }
            if (msEndPoint.latY > maxLat) {
                maxLat = msEndPoint.latY;
            }



            MissingSegment ms = msEndPoint.msBelonged;
            //System.out.println("msBelonged.mmsi = " + ms.mmsi);
            if (msEndPoint.ifSmallerLonX == true) {

                //System.out.println("smallerLonx, i = " + i + ", " + DataReading.missingSegmentEndPointList.get(i).lonX);

                // when stopping at the start of an MS, add it		
                // add itself to the doubly-linked list and the hashmap
                BeadIntersection newSizeOneObject = new BeadIntersection(ms);
                msObservedLinkedList.add(newSizeOneObject);
                msToBeadIntersectionMap.put(ms, new ArrayList<>());
                msToBeadIntersectionMap.get(ms).add(newSizeOneObject);

                // add the intersection between all the existing objects
                int msObservedLinkedListCurrentSize = msObservedLinkedList.size();
                //System.out.println("currentSize = " + msObservedLinkedListCurrentSize);
                int tmpCount = 1;

                ArrayList<BeadIntersection> biIntersectionToBeAdded = new ArrayList<>();

                Iterator<BeadIntersection> biIterator = msObservedLinkedList.iterator(); // loop for all the existing beadintersections use iterator to reduce the cost
                while (biIterator.hasNext()) { // use iterator here, do NOT use for loop		
                    //System.out.println("tmpCount = " + tmpCount);
                    if (tmpCount >= msObservedLinkedListCurrentSize) { // the iteration reaches the end of the list given
                        //System.out.println("exit the loop!");
                        break;
                    }
                    tmpCount++;

                    //System.out.println("list size = " + msObservedLinkedList.size());
                    BeadIntersection biElement = biIterator.next();
                    //BeadIntersection newBI = null; // for debug
                    BeadIntersection newBI = myBI.addMStoBI(ms, biElement); // this function is able to determine if two BI belong to same ship
                    if (newBI == null) { //not intersected
                        //System.out.println("NOT intersected!");
                        countFail += biElement.msInvolvedList.size();
                        continue;
                    }
                    else { // intersected, we have a new CR
                        //System.out.println("intersected!");

//						biElement.ifOutput = false;
//						newBI.ifOutput = true;
                        countPass += biElement.msInvolvedList.size();

                        resultCRs.add(newBI); // add to results List;
                        biIntersectionToBeAdded.add(newBI); // use this one instead the one below to avoid concurrent iterator exception
                        for (MissingSegment tmpMs: biElement.msInvolvedList) {  // add to the hashmap
                            msToBeadIntersectionMap.get(tmpMs).add(newBI);
                        }
                        // add a new element in the list
                        msToBeadIntersectionMap.get(ms).add(newBI);
                    }
                }
                msObservedLinkedList.addAll(biIntersectionToBeAdded);
            }
            else { // when stopping at the end of an ME, remove all the corresponding objects from the current list

                // remove from doubly linked list
                ArrayList<BeadIntersection> objectsToBRemoved = msToBeadIntersectionMap.get(ms);

                for (BeadIntersection obj : objectsToBRemoved) {
                    msObservedLinkedList.remove(obj);
                }
                // remove from the hashmap
                msToBeadIntersectionMap.remove(ms);
            }
            //System.out.println("end loop size = " + msObservedLinkedList.size() + "\n");
        }
        long endTime = System.nanoTime();
        System.out.println("total time = " + (endTime - startTime) / 1000000.0 + " milliseconds");

        BufferedWriter bw = new BufferedWriter(new FileWriter(FilePathsAndParameters.outputClanRendFilePath));
        bw.write("startTime,endTime,involved mmsi\n");
        for (BeadIntersection cr : resultCRs) {
//			if (cr.ifOutput == false) { // when we do not want to output all the CRs
//				continue;
//			}
            bw.write(cr.startTime+","+cr.endTime+","+cr.msInvolvedList.size()+",");
            for (MissingSegment involvedMS : cr.msInvolvedList) {
                bw.write(involvedMS.mmsi+",");
            }
            bw.write("\n");
        }
        bw.close();
        System.out.println("minLon = " + minLon + ", maxLon = " + maxLon + ", minLat = " + minLat + ", maxLat = " + maxLat);
        System.out.println("countPass = " + countPass + ", countFail = " + countFail);
    }
    public static void main(String[] args) {
        BaselinePlaneSweepApproach myBaselinePlaneSweepApproach = new BaselinePlaneSweepApproach();
    }
}
