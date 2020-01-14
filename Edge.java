/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * Edge.java
 * An edge is represented as either a ray or a line segment. The ray
 * representation consists of a starting point and an orientation specified
 * by the sites to its immediate left and right. Direction is (+1) if the ray
 * points in the nonegative x direction, and (-1) otherwise. A line segment
 * representation is specified by two endpoints.
 */

public class Edge {

  Point start, end; // stard and end points
  Node left, right; // the site nodes to the left and right of the edge

  // initialize an edge as a ray
  public Edge(Point start, Node left, Node right) {
    this.start = start;
    this.left = left;
    this.right = right;
  }

  // initialize an edge as a line segment
  public Edge(Point start, Point end, Node left, Node right) {
    this.start = start;
    this.end = end;
    this.left = left;
    this.right = right;
  }

  // returns the x-component of the direction (from start to end)
  public double direction() {
    if (left.site.y >= right.site.y) return 1;
    else return -1;
  }

  // returns the slope of this edge
  public double slope() {
    if (left.site.slope(right.site) == 0) return Double.POSITIVE_INFINITY;
    else return -1 / left.site.slope(right.site);
  }

  // returns the point of intersection of two edges, null if it doesn't exist
  public Point intersection(Edge that) {
    // calculate the intersection of the two lines forming the edges
    double this_s = this.slope();
    double this_x = this.start.x;
    double this_y = this.start.y;
    double that_s = that.slope();
    double that_x = that.start.x;
    double that_y = that.start.y;

    double x, y;
    // case: parallel lines
    if (this_s == that_s) return null;
    // case: one of the lines is vertical
    else if (this_s == Double.POSITIVE_INFINITY) {
      x = this_x;
      y = that_y + that_s * (that_x - this_x);
    } else if (that_s == Double.POSITIVE_INFINITY) {
      x = that_x;
      y = this_y + this_s * (that_x - this_x);
    }
    // case: normal intersection
    else {
      x = (this_s * this_x - that_s * that_x + that_y - this_y) /
                  (this_s - that_s);
      y = (this_s * that_y - that_s * this_y + this_s * that_s * (this_x - that_x)) /
                  (this_s - that_s);
    }

    // check if the intersection lies on at least both edges (rays)
    if ((x - this_x) / direction() >= 0 && (x - that_x) / that.direction() >= 0) {
      Point intersection = new Point(x, y);
      return intersection;
    } else return null;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (end == null) sb.append(start + " to (--, --)"); // start only
    else sb.append(start + " to " + end); // start and end

    return sb.toString();
  }
}
