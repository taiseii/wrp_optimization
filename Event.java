//package Week4ANoNetwork;

public class Event {

  /* Arrival Event */
  public static final int ARRIVAL = 1;
  /* Departure Event */
  public static final int DEPARTURE = 2;
  /* Abandonment Event */
  public static final int ABANDONMENT = 3;

  public static final int VDEPARTURE = 4;

  /* Event type */
  protected int type;
  /* Event time */
  protected double time;
  /* Customer */
  protected Customer customer;

  /**
   * Constructs an Event
   * @param type the Event type (ARRIVAL or DEPARTURE)
   * @param time the Event time
   * @param customer the customer that this event is associated with. Specify 'null' if no customer is provided.
   */
   
  public Event(int type, double time, Customer customer) {
    this.type = type;
    this.time = time;
    this.customer = customer;
  }
  
  /**
   * Returns the event type.
   * @return event type
   */
  
  public int getType() {
    return type;
  }
  
  /**
   * Returns the event time.
   * @return event time
   */

  public double getTime() {
    return time;
  }
  
  public Customer getCustomer() {
      return customer;
  }
  
  public String toString() {
      String[] typeStr = {"", "ARRIVAL", "DEPARTURE", "ABANDONMENT"};
      return typeStr[type]+" at t = "+time+" for customer "+customer;
  }
  
} 