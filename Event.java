/**
 * COS 451 FINAL PROJECT
 * Fall 2019, Prof. Chazelle
 * Sharon Zhang (sharonz)
 *
 * Event.java
 * An event has type SITE or CIRCLE. SITE events occur when the sweepline
 * encounters a new site. CIRCLE events occur when the sweepline is tangent to
 * a circle of three sites, resulting in a Voronoi vertex being formed. Events
 * are compared chronologically by increasing y-coordinate of their site.
 */

public class Event implements Comparable<Event> {
  public enum EventType {
    SITE, CIRCLE
  }

  EventType type; // SITE or CIRCLE
  boolean isValid; // true iff event should be processed (CIRCLE events only)
  Point site; // the occurence location of this event
  Node mid; // the squeezed arc (CIRCLE events only)
  Node arcNode; // the arc on the beachline for this event (CIRCLE events only)

  // initialize a SITE event
  public Event(Point site) {
    this.site = site;
    type = EventType.SITE;
  }

  // initialize a CIRCLE event
  public Event(Node mid, Point site) {
    this.mid = mid;
    this.site = site;
    isValid = true;
    type = EventType.CIRCLE;
  }

  // compare this event to another event
  public int compareTo(Event that) {
    return this.site.compareTo(that.site);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (type == EventType.SITE) {
      sb.append(String.format("Event type: %s", "SITE\n")); // type
      sb.append(String.format("Event site: %s\n", site.id)); // site
    } else {
      sb.append(String.format("Event type: %s", "CIRCLE\n")); // type
      sb.append(String.format("Is valid: " + isValid + "\n")); // isValid
      sb.append(String.format("Squeezed arc: " + mid + "\n")); // mid
    }

    return sb.toString();
  }

}
