package rendezvous;

public class MissingSegmentEndPoint {

    double lonX;
    double latY;
    boolean ifStartPoint; // true = start, false = end. The start here means the earlier / later endpoint
    boolean ifSmallerLonX; // true = smaller LonX, false = larger lonX. Useful in the plane sweep based on lonX
    MissingSegment msBelonged; // which segment it is
    int lineCount;

    public MissingSegmentEndPoint(double lonX, double latY, boolean ifStartPoint, MissingSegment msBelonged) {
        this.lonX = lonX;
        this.latY = latY;
        this.ifStartPoint = ifStartPoint;
        this.ifSmallerLonX = false; // set to false by default
        this.msBelonged = msBelonged;
    }
}
