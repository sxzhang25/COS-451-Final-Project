/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * WaterPattern.java
 * Simulates the bottom of a pool in the hot summer months or a bunch of bubbles!
 *
 * Takes in three command line arguments:
 * (1) numPoints: the number of sites for the voronoi diagram (I recommend no
 *                less than 30)
 * (2) numFrames: the number of frames you wish to generate
 * (3) loop: the number of times to loop the generated animation
 *
 * Recommended inputs: (50, 50, 4)
 */

import java.util.List;
import java.awt.Color;
import java.util.Random;

public class WaterPattern {

  // generates and returns a list of n random points
  public static Point[] generateRandomSites(int n) {
    Point[] sites = new Point[n];
    for (int i = 0; i < n; i++) {
      double x = 400 * Math.random();
      double y = 400 * Math.random();
      sites[i] = new Point(x, y);
    }

    return sites;
  }

  // jitters this point around slightly
  public static Point perturb(Point p, int k) {
    Random rand = new Random();
    double xe = p.x + k * rand.nextGaussian();
    double ye = p.y + k * rand.nextGaussian();
    Point perturbed = new Point(xe, ye);
    return perturbed;
  }

  // jitters this point around slightly (with drift)
  public static Point perturb(Point p, int k, int d) {
    Random rand = new Random();
    double xe = p.x + k * rand.nextGaussian() + d;
    double ye = p.y + k * rand.nextGaussian() + d;
    Point perturbed = new Point(xe, ye);
    return perturbed;
  }

  // jitters the points around slightly
  public static Point[] perturb(Point[] sites, int k) {
    Point[] perturbedSites = new Point[sites.length];
    for (int i = 0; i < sites.length; i++) {
      Random rand = new Random();
      Point perturbed = perturb(sites[i], k);
      perturbedSites[i] = perturbed;
    }

    return perturbedSites;
  }

  public static void createFrames(int numPoints, int numFrames) {
    // create voronoi diagrams
    Point[] sites1 = generateRandomSites(numPoints);
    Point[] sites2 = perturb(sites1, 15);
    VoronoiDiagram vd1 = new VoronoiDiagram(sites1);
    VoronoiDiagram vd2 = new VoronoiDiagram(sites2);
    Point innertube = new Point(100 + 100 * Math.random(), 100 + 100 * Math.random());

    StdDraw.setCanvasSize(400, 400);
    StdDraw.setScale(0, 400);

    List<Edge> edges1 = vd1.getVoronoiEdges();
    List<Edge> edges2 = vd2.getVoronoiEdges();

    // create the frames
    for (int i = 0; i < numFrames; i++) {
      StdDraw.clear(new Color(61, 208, 249));

      // draw light scatters
      StdDraw.setPenColor(new Color(141, 238, 249, 120));
      StdDraw.setPenRadius(0.01);
      for (Edge e : edges1) StdDraw.line(e.start.x, e.start.y, e.end.x, e.end.y);

      StdDraw.setPenColor(new Color(142, 233, 252, 45));
      StdDraw.setPenRadius(0.0075);
      for (Edge e : edges2) StdDraw.line(e.start.x, e.start.y, e.end.x, e.end.y);

      // draw inner tube!
      StdDraw.picture(innertube.x, innertube.y, "innertube.png", 125, 125);

      // save the frame
      StdDraw.save("frame" + i + ".png");

      // perturb sites and innertube position
      sites1 = perturb(sites1, 3);
      sites2 = perturb(sites2, 7);
      vd1 = new VoronoiDiagram(sites1);
      vd2 = new VoronoiDiagram(sites2);
      edges1 = vd1.getVoronoiEdges();
      edges2 = vd2.getVoronoiEdges();
      innertube = perturb(innertube, 1, 1);
    }
  }

  // plays the animation
  public static void playFrames(int numFrames, int loops) {
    StdDraw.setCanvasSize(400, 400);
    StdDraw.setScale(0, 400);

    for (int i = 0; i < loops; i++) {
      for (int j = 0; j < numFrames; j++) {
        StdDraw.picture(200, 200, "frame" + j + ".png", 400, 400);
        StdDraw.pause(100);
      }
    }
  }

  public static void main(String[] args) {
    // generate random points in grid
    int numPoints = Integer.parseInt(args[0]);
    int numFrames = Integer.parseInt(args[1]);
    int loops = Integer.parseInt(args[2]);

    // createFrames(numPoints, numFrames); // uncomment to make new frames
    playFrames(numFrames, loops);
  }
}
