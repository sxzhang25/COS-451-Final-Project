/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * VoronoiDemo.java
 * File used to test and run the VoronoiDiagram class.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.awt.Color;

public class VoronoiDemo {

  // generates and returns a list of n random points
  public static Point[] generateRandomSites(int n) {
    Point[] sites = new Point[n];
    Random rand = new Random();
    for (int i = 0; i < n; i++) {
      double x = rand.nextDouble();
      double y = rand.nextDouble();
      sites[i] = new Point(x, y);
    }

    return sites;
  }

  public static void main(String[] args) {
    // generate random points in grid
    int n = Integer.parseInt(args[0]);
    Point[] sites = generateRandomSites(n);
    VoronoiDiagram vd = new VoronoiDiagram(sites);

    // stopwatch for timing algorithm
    Stopwatch timer = new Stopwatch();

    // perform Fortune's Sweepline algorithm
    List<Edge> edges = vd.getVoronoiEdges();
    double time = timer.elapsedTime();

    System.out.println("Time taken: " + time);
    // vd.printVoronoiEdges();

    // draw voronoi diagram (uncomment to show)
    StdDraw.setCanvasSize(600, 600);
    StdDraw.setPenRadius(0.005);
    StdDraw.setPenColor(Color.BLACK);
    for (Point p : sites) StdDraw.point(p.x, p.y);

    StdDraw.setPenRadius(0.001);
    Edge[] edges_arr = edges.toArray(new Edge[0]);
    for (Edge e : edges_arr) StdDraw.line(e.start.x, e.start.y, e.end.x, e.end.y);
  }
}
