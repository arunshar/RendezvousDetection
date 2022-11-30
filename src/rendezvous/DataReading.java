package rendezvous;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

class SortByLonX implements Comparator<MissingSegmentEndPoint> {
    public int compare (MissingSegmentEndPoint a, MissingSegmentEndPoint b) {
        if (a.lonX > b.lonX) {
            return 1;
        }
        else if (a.lonX < b.lonX) {
            return -1;
        }
        else {
            return 0;
        }
    }
}


public class DataReading {
    ArrayList<String> originalLineList;

    static ArrayList<MissingSegment> missingSegmentList;
    static ArrayList<MissingSegmentEndPoint> missingSegmentEndPointList;

    public void readMissingSegments () throws IOException {

        originalLineList = new ArrayList<>();

        BufferedReader brMissingSegments = new BufferedReader(new FileReader(FilePathsAndParameters.inputMissingSegmentFilePath));
        missingSegmentList = new ArrayList<>();

        brMissingSegments.readLine(); // skip the title line
        String line = "";

        int lineCount = 0;

        while ((line = brMissingSegments.readLine()) != null) {

            originalLineList.add(line);

            String [] components = line.split(",");
            double lonXStart = Double.parseDouble(components[0]);
            double latYStart = Double.parseDouble(components[1]);
            double timeStart = Double.parseDouble(components[2]);
            double lonXEnd = Double.parseDouble(components[3]);
            double latYEnd = Double.parseDouble(components[4]);
            double timeEnd = Double.parseDouble(components[5]);
            String mmsi = components[6];

            MissingSegment ms = new MissingSegment(lonXStart, latYStart, timeStart, lonXEnd, latYEnd, timeEnd, mmsi);

            ms.lineCount = lineCount;

            missingSegmentList.add(ms);

            lineCount++;
        }
        brMissingSegments.close();

        missingSegmentEndPointList = new ArrayList<>();
        for (int i = 0; i < missingSegmentList.size(); i++) {
            MissingSegmentEndPoint msEndPointStart = new MissingSegmentEndPoint(missingSegmentList.get(i).lonXStart, missingSegmentList.get(i).latYStart,
                    true, missingSegmentList.get(i));
            MissingSegmentEndPoint msEndPointEnd = new MissingSegmentEndPoint(missingSegmentList.get(i).lonXEnd, missingSegmentList.get(i).latYEnd,
                    false, missingSegmentList.get(i));

            if (msEndPointStart.lonX <= msEndPointEnd.lonX) { // if the start endpoint is swept earlier
                msEndPointStart.ifSmallerLonX = true;
            }
            else { // if the end endpoint is swept earlier
                msEndPointEnd.ifSmallerLonX = true;
            }

            msEndPointStart.lineCount = missingSegmentList.get(i).lineCount;
            msEndPointEnd.lineCount = missingSegmentList.get(i).lineCount;

            missingSegmentEndPointList.add(msEndPointStart);
            missingSegmentEndPointList.add(msEndPointEnd);
        }

        System.out.println(missingSegmentList.size() + " missing segment read and put in the missing segmnet end point list!");


        // Sort the missingSegmentEndPointList
        Collections.sort(missingSegmentEndPointList, new SortByLonX());
        System.out.println("missingSegmentEndPointList sorted!\n");


        HashSet<String> resultSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            resultSet.add(originalLineList.get(missingSegmentEndPointList.get(i).lineCount));
        }


    }

    public static void main(String[] args) throws IOException {
        DataReading dr = new DataReading();
        dr.readMissingSegments();
    }
}
