/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * Node.java
 * This class represents a node of the beachline. An ARC node represents a part
 * of a parabola. An EDGE node represents a growing edge (ray).
 */

public class Node {
  public enum NodeType {
    ARC, EDGE
  }

  NodeType type; // ARC or EDGE
  Node left, right, parent; // the left and right children and parent nodes
  Edge edge; // if this node represents an edge
  Point site; // if this node represents a site
  Event circleEvent; // the circle event of this node (if it is a site node)

  // initialize an arc Node
  public Node(Point site) {
    this.site = site;
    type = type.ARC;
  }

  // initialize an edge Node
  public Node(Edge edge) {
    this.edge = edge;
    type = type.EDGE;
  }

  // returns true if this node is a leaf, false otherwise
  public boolean isLeaf() {
    return left == null && right == null;
  }

  // sets the left child of this node
  public void setLeft(Node node) {
    this.left = node;
    node.parent = this;
  }

  // sets the right child of this node
  public void setRight(Node node) {
    this.right = node;
    node.parent = this;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (type == NodeType.ARC) {
      sb.append(String.format("Node type: ARC\n")); // type
      sb.append(String.format("Site: " + site.id + "\n")); // site
      if (circleEvent != null) sb.append(String.format("Circle event: YES\n")); // circleEvent
      else sb.append(String.format("Circle event: NO\n"));
    } else {
      sb.append(String.format("Node type: EDGE\n")); // type
      sb.append(String.format("Edge: " + edge + "\n")); // edge
    }

    return sb.toString();
  }
}
