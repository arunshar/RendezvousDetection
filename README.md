# Clandestine Rendezvous Detection

## Introduction

Given multiple trajectories which have gaps due to weak signals, instrument malfunction or malicious interference we find possible possible times and places where moving objects (e.g. ships) rendezvous or meetup. Figure below shows an example of the input and output. For simplicity, we are using one-dimensional geographical space along with the dimension of time. Object 1 is shown in blue and Object 2 is shown in red. The gaps are shown in a dotted form and lie between P3, P4 for blue and P4, P5 for red. Object 1 has a maximum speed of 1 and Object 2 has a maximum speed of 2. The output shows the candidate active volume (CAV) for each object. A CAV is the region in a gap that represents all the possible locations of the object during a missing time interval. The intersection of the two CAVs is the possible rendezvous region termed as a spatio-temporal intersection (STI).

![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/InputOutput.png)


### Basic Concept

An object maximum speed (Smax) is the maximum speed of an object 1 based on the domain knowledge.

An effective missing period (EMP) or MissingSegment is a time period when the signal is missing for longer than a user-specified EMP threshold (θe). As shown in Figure 1 the EMPs for Object 1 and Object 2 is between timestamps 3 and 4, Here, we assume θe = 2.

A candidate active volume (CAV) or Bead (in one Dimension) is the spatio-temporal volume where an object is possibly located during an EMP. A CAV is based on a space time prism using conical shape and is derived from an EMP. The 1D representation of conical intersection of 

<!-- ![Image](https://github.com/arunshar/Distributed-Systems/blob/master/GroupMessenger2/images/ISIS_Algorithm_Working.gif) -->
![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/CAV_and_Bead.png)

### Baseline Algorithm
 We use the plane sweep algorithm for extracting gaps. It is a filter and refine approach where the given study area is projected into a lower-dimensional space. In the filtering phase, all gaps are sorted based on x or y coordinate. Ordering on one dimension reduces the storage and I/O cost, and further allows the computation of intersections in a single pass. In the refinement phase, the gaps are extracted based on the start time and end time of their respective effective missing periods (EMPs). The segments are further approximated using MOBRs over each candidate active volume (CAV). The following text describes the algorithm in detail.

Step 1: Sort the endpoints of all the effective missing periods (EMPs)
First, we sort the endpoints of all EMPs based on one of the coordinates. An endpoint is represented by three coordinates, namely x, y, and time, and either x or y can be the sorting coordinate. For consistency, We use x dimension in the code 

Step 2: A plane orthogonal to the x-axis sweeps along the sorted EMPs
The second step conducts the sweeping.
Imagine there is a plane parallel to the y-t plane and orthogonal to the x-axis sweeping from the low to the high end along x-axis. The sweeping plane stops at both start and end endpoints of each EMP. Note that  start and end refer to the order of sweeping, which is irrelevant to the temporal dimension. An Observed Object List is maintained to store CAVs being currently crossed by the sweeping plane.

![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/Intersections.png)

Figure above shows a dataset contains EMPs and their corresponding CAVs (or Beads) from four objects. We simplify the study area into onedimensional for illustrative purpose. A vertical line sweeps from left to right and stops at the endpoints (from a to h) of each EMP. The table on the right shows the elements in the observed object list after each stop. For example, when stopping at Line d (start of 4), the algorithm determines whether the incoming EMP<4> intersects with each element in the observed object list, namely
<1>, <2>, <1,2>, <3>, <1,3>, <2,3>, and <1,2,3>. Among those, since EMP <2> intersects with <4>, <4> and <2, 4> are added to the list. Also, < 2, 4 > is added to the output
list as well. When stopping at Line e (end of 1), the algorithm removes all the elements in the list involving EMP 1 including <1>, <1,2>, <1,3>, and <1,2,3>. After that, the elements left are <2>, <3>, <2, 3>, <4>, and <2, 4>. The last stop is at Line h (end of 4). The Observed Object List becomes empty and the final output STIs include: <1, 2>, <1, 3>, <2, 3>, <1, 2, 3>, and <2, 4>.

### Bead Intersection Criteria

The criteria can be derived from the equation of sum of the radius of two circles should be greater than distance between their centers as shown below:

![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/Intersection_Criteria.png)

Given two segments a (start point 1 and end point 2) and b (start point 3 and end point 4) each with speed, timeStart, and timeEnd.

If both a and b are measured from start point 1 (a.timeStart) and 3 (b.timeStart) at time instant t, then:

a.speed*(t - a.timeStart) + b.speed*(t - a.timeStart) >= d13, where t > timeStart and d13 is distance between the centers of a and b.

t * (-a.speed - b.speed) >= d13 + a.timeStart * a.speed + b.timeStart * b.speed.

t <= (d13 + a.timeStart * a.speed + b.timeStart * b.speed)/ (a.speed + b.speed).

Similar operations are done for (1 (a.timeStart) and 4 (b.timeEnd)), (2 (a.timeEnd) and 3 (a.timeStart)), and (2 (a.timeEnd) and 4 (a.timeEnd)).

If all conditions are satisfied then beads are guaranteed to be intersected.

## Implementation Details

Run Main.java to run the overall code.



## Code

For implementation, please refer to java files [here](https://github.com/arunshar/RendezvousDetection/tree/main/src/rendezvous).

Input Data details can be found [here](https://github.com/arunshar/RendezvousDetection/blob/main/gaps_tsrelative_t30_noNoise.csv) 
