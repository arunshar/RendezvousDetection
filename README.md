# Clandestine Rendezvous Detection

## Introduction

Given multiple trajectories which have gaps due to weak signals, instrument malfunction or malicious interference we find possible possible times and places where moving objects (e.g. ships) rendezvous or meetup. Figure below shows an example of the input and output. For simplicity, we are using one-dimensional geographical space along with the dimension of time. Object 1 is shown in blue and Object 2 is shown in red. The gaps are shown in a dotted form and lie between P3, P4 for blue and P4, P5 for red. Object 1 has a maximum speed of 1 and Object 2 has a maximum speed of 2. The output shows the candidate active volume (CAV) for each object. A CAV is the region in a gap that represents all the possible locations of the object during a missing time interval. The intersection of the two CAVs is the possible rendezvous region termed as a spatio-temporal intersection (STI).

![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/InputOutput.png)


### Basic Concept

An object maximum speed (Smax) is the maximum speed of an object 1 based on the domain knowledge.

An effective missing period (EMP) or MissingSegment is a time period when the signal is missing for longer than a user-specified EMP threshold (θe). As shown in Figure 1 the EMPs for Object 1 and Object 2 is between timestamps 3 and 4, Here, we assume θe = 2.

A candidate active volume (CAV) or Bead (in ID) is the spatio-temporal volume where an object is possibly located during an EMP. A CAV is based on a space time prism using conical shape and is derived from an EMP. The 1D representation of conical intersection of 

<!-- ![Image](https://github.com/arunshar/Distributed-Systems/blob/master/GroupMessenger2/images/ISIS_Algorithm_Working.gif) -->
![Image](https://github.com/arunshar/RendezvousDetection/blob/main/images/CAV_and_Bead.png)

### Baseline Algorithm
 We use the plane sweep algorithm for extracting gaps. It is a filter and refine approach where the given study area is projected into a lower-dimensional space. In the filtering phase, all gaps are sorted based on x or y coordinate. Ordering on one dimension reduces the storage and I/O cost, and further allows the computation of intersections in a single pass. In the refinement phase, the gaps are extracted based on the start time and end time of their respective effective missing periods (EMPs). The segments are further approximated using MOBRs over each candidate active volume (CAV). The following text describes the algorithm in detail.

Step 1: Sort the endpoints of all the effective missing periods (EMPs)
First, we sort the endpoints of all EMPs based on one of the coordinates. An endpoint is represented by three coordinates, namely x, y, and time, and either x or y can be the sorting coordinate. For consistency, We use x dimension in the code 

Step 2: A plane orthogonal to the x-axis sweeps along the sorted EMPs
The second step conducts the sweeping.
Imagine there is a plane parallel to the y-t plane and orthogonal to the x-axis sweeping from the low to the high end along x-axis. The sweeping plane stops at both start and end endpoints of each EMP. Note that  start and end refer to the order of sweeping, which is irrelevant to the temporal dimension. An Observed Object List is maintained to store CAVs being currently crossed by the sweeping plane.


## Implementation Details

Main.java: 


## Code

For implementation, please refer to java files [here](https://github.com/arunshar/RendezvousDetection/tree/main/src/rendezvous).

Input Data details can be found [here](https://github.com/arunshar/RendezvousDetection/blob/main/gaps_tsrelative_t30_noNoise.csv) 
