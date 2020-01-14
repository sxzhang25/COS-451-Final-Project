/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * Point.java
 * A point is represented in standard Cartesian coordinates.
 */

public class Point implements Comparable<Point> {

  double x, y; // x and y-coordinates of this point
  String id; // id of this point/site (for debugging purposes)

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // calculate the slope from this point to that point
  public double slope(Point that) {
    if (that.x == this.x) return Double.POSITIVE_INFINITY;
    else return (that.y - this.y) / (that.x - this.x);
  }

  // compare this point to another point
  public int compareTo(Point that) {
    if (this.y > that.y) return 1;
    else if (this.y < that.y) return -1;
    else {
      if (this.x > that.x) return 1;
      else if (this.x < that.x) return -1;
      else return 0;
    }
  }

  public String toString() {
    return String.format("(%.2f, %.2f)", x, y);
  }
}
