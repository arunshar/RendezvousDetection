package rendezvous;

import java.util.ArrayList;

class TimeRange {
    double startTime;
    double endTime;

    public TimeRange (double startTime, double endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

public class BeadIntersection {
    ArrayList<MissingSegment> msInvolvedList; // the missing segment (EMP in the paper) in this intersection
    double startTime = 0;
    double endTime = 0;
    boolean ifOutput = false; // used for labeling if we output this CR
    //double centerLonX = 0;
    //double centerLatY = 0;

    public TimeRange twoMSIntersect (MissingSegment a, MissingSegment b) {
        // if two beads intersect, return the time range.
        // if not intersected, return null
        // called by addMStoBI()

        this.msInvolvedList = new ArrayList<>();
        if ( (a.timeEnd <= b.timeStart) || (a.timeStart >= b.timeEnd) ) {
            return null; // time of the two beads not overlapped
        }

        double timeMin = Math.max(a.timeStart, b.timeStart);
        double timeMax = Math.min(a.timeEnd, b.timeEnd);
        //System.out.println("timeMin = " + timeMin + ", timeMax = " + timeMax);

        double d13 = Utilities.distanceTwoPoints(a.latYStart, a.lonXStart, b.latYStart, b.lonXStart);
        double d24 = Utilities.distanceTwoPoints(a.latYEnd, a.lonXEnd, b.latYEnd, b.lonXEnd);
        double d14 = Utilities.distanceTwoPoints(a.latYStart, a.lonXStart, b.latYEnd, b.lonXEnd);
        double d23 = Utilities.distanceTwoPoints(a.latYEnd, a.lonXEnd, b.latYStart, b.lonXStart);
        //System.out.println(d13+", "+d24+", "+d14 + "," + d23);

        if (a.speed > b.speed) {
            double cond1 = (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed);
            double cond2 = (d14 + a.timeStart * a.speed - b.timeEnd * b.speed) / (a.speed - b.speed);
            double cond3 = (d23 + b.timeStart * b.speed - a.timeEnd * a.speed) / (b.speed - a.speed);
            double cond4 = (d24 - a.timeEnd * a.speed - b.timeEnd * b.speed) / (-a.speed - b.speed);

            timeMin = Math.max(timeMin, Math.max(cond1, cond2));
            timeMax = Math.min(timeMax, Math.min(cond3, cond4));
        }

        else if (a.speed < b.speed) {
            double cond1 = (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed);
            double cond2 = (d14 + a.timeStart * a.speed - b.timeEnd * b.speed) / (a.speed - b.speed);
            double cond3 = (d23 + b.timeStart * b.speed - a.timeEnd * a.speed) / (b.speed - a.speed);
            double cond4 = (d24 - a.timeEnd * a.speed - b.timeEnd * b.speed) / (-a.speed - b.speed);

            timeMin = Math.max(timeMin, Math.max(cond1, cond3));
            timeMax = Math.min(timeMax, Math.min(cond2, cond4));
        }
        else { // a.speed = b.speed

            if ( ((b.timeEnd - a.timeStart) * a.speed >= d14) && (a.timeEnd - b.timeStart) * a.speed >= d23 ) {
                //System.out.println((b.timeEnd - a.timeStart) * a.speed + ", " + (a.timeEnd - b.timeStart) * a.speed);
                timeMin = Math.max(timeMin, (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed));
                timeMax = Math.min(timeMax, (d24 - a.timeEnd * a.speed - b.timeEnd * b.speed) / (-a.speed - b.speed));
                //System.out.println("timeMin = " + timeMin + ", timeMax = " + timeMax);
            }
            else { // just to create a case where timeMin > timeMax
                timeMin = 1;
                timeMax = 0;
            }
        }

        if (timeMin <= timeMax) { // these two are intersected
            return new TimeRange(timeMin, timeMax);
        }
        return null;
    }

    public BeadIntersection () {
        this.msInvolvedList = new ArrayList<>();
        this.startTime = 0;
        this.endTime = 0;
        //centerLonX = 0;
        //centerLatY = 0;
    }

    public BeadIntersection (MissingSegment a) {
        // only one missing segment
        this.msInvolvedList = new ArrayList<>();
        this.msInvolvedList.add(a);
        this.startTime = a.timeStart;
        this.endTime = a.timeEnd;
        //this.centerLonX = a.lonXStart + (a.lonXEnd - a.lonXStart) / 2;
        //this.centerLatY = a.latYStart + (a.latYStart - a.latYEnd) / 2;
    }

    // a heuristic that determines when and where two beads intersect
    public BeadIntersection (MissingSegment a, MissingSegment b) {
        // construct a bead intersection from two beads

        this.msInvolvedList = new ArrayList<>();
        if ( (a.timeEnd <= b.timeStart) || (a.timeStart >= b.timeEnd) ) {
            return; // time of the two beads not overlapped
        }

        double timeMin = Math.max(a.timeStart, b.timeStart);
        double timeMax = Math.min(a.timeEnd, b.timeEnd);

        double d13 = Utilities.distanceTwoPoints(a.latYStart, a.lonXStart, b.latYStart, b.lonXStart);
        double d24 = Utilities.distanceTwoPoints(a.latYEnd, a.lonXEnd, b.latYEnd, b.lonXEnd);
        double d14 = Utilities.distanceTwoPoints(a.latYStart, a.lonXStart, b.latYEnd, b.lonXEnd);
        double d23 = Utilities.distanceTwoPoints(a.latYEnd, a.lonXEnd, b.latYStart, b.lonXStart);

        if (a.speed > b.speed) {
            double cond1 = (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed);
            double cond2 = (d14 + a.timeStart * a.speed - b.timeEnd * b.speed) / (a.speed - b.speed);
            double cond3 = (d23 + b.timeStart * b.speed - a.timeEnd * a.speed) / (b.speed - a.speed);
            double cond4 = (d24 - a.timeEnd * a.speed - b.timeEnd * b.speed) / (-a.speed - b.speed);

            timeMin = Math.max(timeMin, Math.max(cond1, cond2));
            timeMax = Math.min(timeMax, Math.min(cond3, cond4));
        }

        else if (a.speed < b.speed) {
            double cond1 = (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed);
            double cond2 = (d14 + a.timeStart * a.speed - b.timeEnd * b.speed) / (a.speed - b.speed);
            double cond3 = (d23 + b.timeStart * b.speed - a.timeEnd * a.speed) / (b.speed - a.speed);
            double cond4 = (d24 + a.timeEnd * a.speed + b.timeEnd * b.speed) / (-a.speed - b.speed);

            timeMin = Math.max(timeMin, Math.max(cond1, cond3));
            timeMax = Math.min(timeMax, Math.min(cond2, cond4));
        }
        else { // a.speed = b.speed

            if ( ((b.timeEnd - a.timeStart) * a.speed >= d14) && (a.timeEnd - b.timeStart) * a.speed >= d23 ) {
                timeMin = Math.max(timeMin, (d13 + a.timeStart * a.speed + b.timeStart * b.speed) / (a.speed + b.speed));
                timeMax = Math.min(timeMax, (d24 + a.timeEnd * a.speed + b.timeEnd * b.speed) / (-a.speed - b.speed));
            }
            else { // just to create a case where timeMin > timeMax
                timeMin = 1;
                timeMax = 0;
            }
        }

        if (timeMin <= timeMax) { // fill the intersection
            this.startTime = timeMin;
            this.endTime = timeMax;
            this.msInvolvedList.add(a);
            this.msInvolvedList.add(b);
        }
        // else do nothing, leave the bead intersection empty
    }


    public BeadIntersection addMStoBI (MissingSegment a, BeadIntersection b) {
        // input a missing segment A and a bead intersection B
        // return the new BI of their intersection
        // return null if not intersected
        this.msInvolvedList = new ArrayList<>();
        double startTime = 0;
        double endTime = 0;
        // the following if is just for debugging
//		if ( (b.startTime <= a.timeStart && a.timeStart <= b.endTime) || (b.startTime <= a.timeEnd && a.timeEnd <= b.endTime) ) {
//
//			for (int i = 0; i < b.msInvolvedList.size(); i++) { // this loops checks if b includes the ship of a
//				if (a.mmsi.equals(b.msInvolvedList.get(i).mmsi)) {
//					return null;
//				}
//			}
//
//			// a new BI is generated
//			BeadIntersection largerBI = new BeadIntersection();
//			largerBI.startTime = Math.max(a.timeStart, b.startTime);
//			largerBI.endTime = Math.min(a.timeEnd, b.endTime);
//			largerBI.msInvolvedList = new ArrayList<MissingSegment>(b.msInvolvedList);
//			largerBI.msInvolvedList.add(a);
//			System.out.println("size = " + largerBI.msInvolvedList.size());
//			return largerBI;
//		}

        //check the intersection time of a and b
        if ( (b.startTime <= a.timeStart && a.timeStart <= b.endTime) || (b.startTime <= a.timeEnd && a.timeEnd <= b.endTime) ) {
            // if the two time intervals intersect
            //check if MS a intersects with b spatially by checking all the elements of b.

            for (int i = 0; i < b.msInvolvedList.size(); i++) { // this loops checks if b includes the ship of a
                if (a.mmsi.equals(b.msInvolvedList.get(i).mmsi)) {
                    return null;
                }
            }

            // current intersected time range
            startTime = Math.max(a.timeStart, b.startTime);
            endTime = Math.min(a.timeEnd, b.endTime);

            for (int i = 0; i < b.msInvolvedList.size(); i++) {
                TimeRange tmpTR = twoMSIntersect (a, b.msInvolvedList.get(i));
                if ( tmpTR == null ) {
                    return null; // not intersected
                }
                else {
                    if (tmpTR.startTime >= endTime || tmpTR.endTime <= startTime) {
                        return null; // not intersected
                    }
                    // update the time range to the intersection
                    startTime = Math.max(startTime, tmpTR.startTime);
                    endTime = Math.min(endTime, tmpTR.endTime);
                }
            }
            // a new BI is generated
            BeadIntersection largerBI = new BeadIntersection();
            largerBI.startTime = startTime;
            largerBI.endTime = endTime;
            largerBI.msInvolvedList = new ArrayList<MissingSegment>(b.msInvolvedList);
            largerBI.msInvolvedList.add(a);
            return largerBI;
        }
        return null;
    }


    public static void main(String[] args) {

    }
}
