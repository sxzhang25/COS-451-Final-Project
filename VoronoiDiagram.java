/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * VoronoiDiagram.java
 * Creates a VoronoiDiagram object from a set of points. The client can
 * calculate the Voronoi edges and print them out.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayDeque;

public class VoronoiDiagram {

  private Queue<Event> eq; // event queue
  private List<Edge> edges; // the voronoi diagram edges
  private Node beachline; // the beachline (binary tree) root
  private double sweepline; // the sweepline (a y-coordinate);

  public VoronoiDiagram(Point[] sites) {
    sweepline = 0;
    edges = new ArrayList<Edge>();

    // initialize event queue and fill with site events
    eq = new PriorityQueue<Event>();
    int i = 1;
    for (Point p : sites) {
      Event e = new Event(p);
      p.id = Integer.toString(i++);
      eq.add(e);
    }
  }

  // returns the voronoi diagram edges
  public List<Edge> getVoronoiEdges() {
    fortuneSweepline();
    return edges;
  }

  // print the voronoi edges out
  public void printVoronoiEdges() {
    System.out.println(String.format("VORONOI EDGES (%d)\n", edges.size()));
    Edge[] edgeList = edges.toArray(new Edge[0]);
    for (Edge edge : edgeList) System.out.println(edge);
  }

  // perform Fortune's Sweepline algorithm
  private void fortuneSweepline() {
    // add all first sites onto beachline (may have multiple starting sites)
    if (!eq.isEmpty()) {
      Event e = eq.poll();
      sweepline = e.site.y;

      // workaround for an edge case: ensure first two nodes will not
      // happen simultaneously
      if (!eq.isEmpty()) {
        if (eq.peek().site.y == sweepline) eq.peek().site.y++;
      }

      // create new node for this site and add to beachline
      Node newArcNode = new Node(e.site);
      beachline = newArcNode;
    }

    while (!eq.isEmpty()) {
      Event e = eq.poll();
      sweepline = e.site.y;
      if (e.type == Event.EventType.SITE) handleSiteEvent(e);
      else if (e.type == Event.EventType.CIRCLE && e.isValid) handleCircleEvent(e);
    }

    // convert remaining edges and arcs on the beachline to voronoi edges
    if (beachline != null) {
      Stack<Node> s = new Stack<>();
      sweepline = Integer.MAX_VALUE;
      s.push(beachline);

      while (!s.isEmpty()) {
        Node node = s.pop();
        if (node.type == Node.NodeType.EDGE) {
          extendEdge(node.edge);
          edges.add(node.edge);
          if (node.left != null) s.push(node.left);
          if (node.right != null) s.push(node.right);
        }
      }
    }
  }

  // handle site events
  private void handleSiteEvent(Event e) {
    // create a new Node for this site
    Node newArcNode = new Node(e.site);

    // find the arc directly above current site in the beachline
    Node belowArcNode = getArcBelow(e.site.x);

    // create two new left and right arc nodes
    Node leftArcNode = new Node(belowArcNode.site);
    Node rightArcNode = new Node(belowArcNode.site);

    // check if we need to remove any circle events from above arc
    if (belowArcNode.circleEvent != null) belowArcNode.circleEvent.isValid = false;

    // create new edge nodes and insert them into the beachline
    double startX = e.site.x;
    double startY = Math.pow(startX - belowArcNode.site.x, 2) /
                      (2 * (belowArcNode.site.y - e.site.y)) +
                      (belowArcNode.site.y + e.site.y) / 2;

    Point newStart = new Point(startX, startY);

    Edge leftEdge = new Edge(newStart, belowArcNode, newArcNode);
    Edge rightEdge = new Edge(newStart, newArcNode, belowArcNode);

    Node leftEdgeNode = new Node(leftEdge);
    Node rightEdgeNode = new Node(rightEdge);

    leftEdgeNode.setLeft(leftArcNode);
    leftEdgeNode.setRight(rightEdgeNode);
    rightEdgeNode.setLeft(newArcNode);
    rightEdgeNode.setRight(rightArcNode);

    if (belowArcNode == beachline) beachline = leftEdgeNode;
    else if (belowArcNode.parent.left == belowArcNode)
      belowArcNode.parent.left = leftEdgeNode;
    else belowArcNode.parent.right = leftEdgeNode;
    leftEdgeNode.parent = belowArcNode.parent;

    // check for circle intersection events
    checkCircleEvent(leftArcNode);
    checkCircleEvent(rightArcNode);
  }

  // handle circle events
  private void handleCircleEvent(Event e) {
    Node leftEdgeNode = getLeftEdge(e.mid);
    Node rightEdgeNode = getRightEdge(e.mid);
    Node leftArcNode = getLeftArc(leftEdgeNode);
    Node rightArcNode = getRightArc(rightEdgeNode);

    // invalidate circle events for adjacent arcs, if necessary
    if (leftArcNode.circleEvent != null) leftArcNode.circleEvent.isValid = false;
    if (rightArcNode.circleEvent != null) rightArcNode.circleEvent.isValid = false;

    // create new node to insert into beachline
    Point center = leftEdgeNode.edge.intersection(rightEdgeNode.edge);
    Edge newEdge = new Edge(center, leftArcNode, rightArcNode);
    Node newEdgeNode = new Node(newEdge);

    // case: parent of squeezed arc is right edge
    if (e.mid.parent == rightEdgeNode) {
      // replace left edge with new edge
      newEdgeNode.setLeft(leftEdgeNode.left);
      newEdgeNode.setRight(leftEdgeNode.right);
      if (beachline == leftEdgeNode) beachline = newEdgeNode;
      else {
        if (leftEdgeNode.parent.left == leftEdgeNode)
          leftEdgeNode.parent.setLeft(newEdgeNode);
        else leftEdgeNode.parent.setRight(newEdgeNode);
      }

      // delete right edge and squeezed arc
      if (rightEdgeNode.parent.left == rightEdgeNode)
        rightEdgeNode.parent.setLeft(rightEdgeNode.right);
      else rightEdgeNode.parent.setRight(rightEdgeNode.right);
    }
    // case: parent of squeezed arc is left edge
    else {
      // replace right edge with new edge
      newEdgeNode.setLeft(rightEdgeNode.left);
      newEdgeNode.setRight(rightEdgeNode.right);
      if (beachline == rightEdgeNode) beachline = newEdgeNode;
      else {
        if (rightEdgeNode.parent.left == rightEdgeNode)
          rightEdgeNode.parent.setLeft(newEdgeNode);
        else rightEdgeNode.parent.setRight(newEdgeNode);
      }

      // delete left edge and squeezed arc
      if (leftEdgeNode.parent.left == leftEdgeNode)
        leftEdgeNode.parent.setLeft(leftEdgeNode.left);
      else leftEdgeNode.parent.setRight(leftEdgeNode.left);
    }

    // add newly formed voronoi edges to edge list
    leftEdgeNode.edge.end = center;
    rightEdgeNode.edge.end = center;
    edges.add(leftEdgeNode.edge);
    edges.add(rightEdgeNode.edge);

    // test newly adjacent arcs for circle events
    checkCircleEvent(getLeftArc(newEdgeNode));
    checkCircleEvent(getRightArc(newEdgeNode));
  }

  // checks for a new circle event squeezing this node
  private void checkCircleEvent(Node arcNode) {
    Node leftEdgeNode = getLeftEdge(arcNode);
    Node rightEdgeNode = getRightEdge(arcNode);

    // left or right edge does not exist
    if (leftEdgeNode == null || rightEdgeNode == null) return;

    // if left and right edges intersect, create a new circle event
    Point center = leftEdgeNode.edge.intersection(rightEdgeNode.edge);
    if (center != null) {
      // calculate circle event y-coordinate based on this intersection
      double dscr = Math.sqrt(Math.pow(center.y - arcNode.site.y, 2) +
                  Math.pow(center.x - arcNode.site.x, 2));
      double dtx = center.y + dscr;
      // do not add if new circle event precedes current time
      if (dtx < sweepline) return;

      Event e = new Event(arcNode, new Point(center.x, dtx));
      arcNode.circleEvent = e;
      eq.add(e);
    }
  }

  // finds the arc node above a given site
  private Node getArcBelow(double x) {
    if (beachline == null) return null;

    Node curr = beachline;
    while (!curr.isLeaf()) {
      double xEnd = getCurrEdgeEnd(curr.edge).x;
      if (x < xEnd) curr = curr.left;
      else curr = curr.right;
    }

    return curr;
  }

  // returns the edge directly to the left of this arc node
  private Node getLeftEdge(Node arcNode) {
    Node curr = arcNode;
    while (curr.parent != null) {
      if (curr.parent.right == curr) return curr.parent;
      else curr = curr.parent;
    }
    return curr.parent;
  }

  // returns the edge directly to the right of this arc node
  private Node getRightEdge(Node arcNode) {
    Node curr = arcNode;
    while (curr.parent != null) {
      if (curr.parent.left == curr) return curr.parent;
      else curr = curr.parent;
    }
    return curr.parent;
  }

  // returns the arc directly to the left of this edge node
  private Node getLeftArc(Node edgeNode) {
    Node curr = edgeNode.left;
    while (!curr.isLeaf()) curr = curr.right;
    return curr;
  }

  // returns the arc directly to the right of this edge node
  private Node getRightArc(Node edgeNode) {
    Node curr = edgeNode.right;
    while (!curr.isLeaf()) curr = curr.left;
    return curr;
  }

  // returns the endpoint of a ray when the sweepline is fixed at a given time
  private Point getCurrEdgeEnd(Edge edge) {
    if (edge.end != null) return edge.end;

    Point f1 = edge.left.site;
    Point f2 = edge.right.site;
    double d = sweepline;

    // find intersection of parabolas
    double x, y;
    if (f1.y == f2.y) { // case: parabola foci are at same level
      x = (f1.x + f2.x) / 2;
      y = Math.pow(x - f1.x, 2) / (2 * (f1.y - d)) + (f1.y + d) / 2;
    } else { // case: normal parabola intersection
      double a = f2.y - f1.y;
      double b = -2 * (f1.x * (f2.y - d) - f2.x * (f1.y - d));
      double c = (f2.y - d) * Math.pow(f1.x, 2) - (f1.y - d) * Math.pow(f2.x, 2) -
                  (f1.y - d) * (f2.y - d) * (f2.y - f1.y);

      double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
      double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);

      x = (edge.direction() > 0) ? Math.max(x1, x2) : Math.min(x1, x2);
      y = Math.pow(x - f1.x, 2) / (2 * (f1.y - d)) + (f1.y + d) / 2;
    }

    Point endpoint = new Point(x, y);
    return endpoint;
  }

  // extend an edge to the border of the window
  private void extendEdge(Edge edge) {
    double xEnd = (edge.direction() > 0) ? Integer.MAX_VALUE : 0;
    double yEnd = edge.slope() * (xEnd - edge.start.x) + edge.start.y;
    edge.end = new Point(xEnd, yEnd);
  }

  /**
   * DEBUGGING METHODS
   */

  // traverse the beachline
  private void inorder(Node node) {
    if (node == null) return;
    inorder(node.left);
    System.out.println(node);
    inorder(node.right);
  }

  // traverse the beachline by level
  private void levelorder() {
    if (beachline == null) return;
    Queue<Node> q = new ArrayDeque<>();
    q.add(beachline);

    int level = 0;
    while (!q.isEmpty()) {
      int size = q.size();
      System.out.println(level++);
      while (size > 0) {
        Node curr = q.poll();
        System.out.println(curr);
        if (curr.left != null) q.add(curr.left);
        if (curr.right != null) q.add(curr.right);
        size--;
      }
    }
  }

  // prints out the parents of each node in level order
  private void printParents() {
    if (beachline == null) return;
    Queue<Node> q = new ArrayDeque<>();
    q.add(beachline);
    int level = 0;
    while (!q.isEmpty()) {
      int size = q.size();
      System.out.println(level++);
      while (size > 0) {
        Node curr = q.poll();
        System.out.println(curr.parent);
        if (curr.left != null) q.add(curr.left);
        if (curr.right != null) q.add(curr.right);
        size--;
      }
    }
  }
}
