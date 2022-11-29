package rendezvous;

public class MissingSegment {

    //

    double lonXStart;
    double latYStart;
    double timeStart;

    double lonXEnd;
    double latYEnd;
    double timeEnd;

    double speed; // max speed: meters per second

    String mmsi;

    int lineCount;

    public MissingSegment(double lonXStart, double latYStart, double timeStart, double lonXEnd, double latYEnd, double timeEnd, String mmsi) {
        this.lonXStart = lonXStart;
        this.latYStart = latYStart;
        this.timeStart = timeStart;

        this.lonXEnd = lonXEnd;
        this.latYEnd = latYEnd;
        this.timeEnd = timeEnd;

        this.speed = FilePathsAndParameters.maxSpeed;

        this.mmsi = mmsi;
    }


    public static void main(String[] args) {

    }

}
