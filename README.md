# COS 451 Final Project
Fortune's Sweepline Algorithm for Voronoi Diagrams
Sharon Zhang (sharonz@princeton.edu)

# DEPENDENCIES
This program uses the `StdDraw.java` and `Stopwatch.java` classes in the Java StdLib.

# HOW TO USE

`VoronoiDemo.java` is the main class to run the algorithm. To use, run:

`java VoronoiDemo.java <num_points>`

where `num_points` is the number of sites (n).


`WaterPattern.java` is a simple client of the `VoronoiDiagram` class. To use, run:

`java WaterPattern.java <numPoints> <numFrames> <loops>`

where `numPoints` is the number of sites for the voronoi diagram (I recommend no less than 30), `numFrames` is the number of frames you wish to generate, and `loop` is the number of times to loop the generated animation.


Recommended input: `java WaterPattern.java 50 50 4`
