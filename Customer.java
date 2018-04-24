//package Week4ANoNetwork;

public class Customer {

    /* The arrival time of this customer in the system */
    protected double arrivalTime;
    protected double systemArrival;
    /* The service time of this customer at the current queue */
    protected double serviceTime;
    /* The time at which this customer's patience runs out */
    protected double endPatienceTime;
    /* Whether the customer has left the system already */
    protected boolean left;

    protected int location;
    protected int pLocation;
    protected int nLocation;
    
    protected int[] route;
    protected double[] trashType;

    protected int carType;

    protected double vqTime;

    


    /**
     * Constructs a Customer with specified arrival time, service time, and
     * patience. The location is set to 0, because each customer arrives at the
     * first queue. The queue arrival time and the system arrival time are both
     * set to the specified arrival time.
     *
     * @param arrivalTime the arrival time
     * @param serviceTime the service time at the first queue
     * @param endPatienceTime the time at which this customer's patience runs
     * out
     */
    public Customer(double arrivalTime, double serviceTime, int[] route, int ct) {
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
        this.systemArrival = arrivalTime;
        this.left = false;
        this.route = route;
        this.location = 0;
        this.pLocation = 0;
        this.nLocation = 0;
        this.vqTime = -1.0;
        this.carType = ct;
        
    }

    /**
     * Returns the arrival time of this customer in the system.
     *
     * @return system arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Indicates that he/she has left the system.
     */
    public void leaveSystem() {
        this.left = true;
    }

    /**
     * Returns if the customer has left.
     * 
     * @return has left
     */
    public boolean hasLeft() {
        return left;
    }

    /**
     * Returns the service time of this customer.
     *
     * @return service time
     */
    public double getServiceTime() {
        return serviceTime;
    }

    /**
     * Returns the time at which this customer's patience runs out
     *
     * @return the end patience time
     */
    public double getEndPatienceTime() {
        return endPatienceTime;
    }

    /**
     * Returns the locatiion at which this car is at
     *
     * @return the end current location
     */
    public int getLocation() {
        return location;
    }

    public void setLocation(int L){
        this.location = L;
      }
    
      public void setnLocation(int L){
        this.nLocation = L;
      }
    
      public int getnLocation(){
        return nLocation;
      }
    
      public void setpLocation(int L){
        this.pLocation = L;
      }
    
      public int getpLocation(){
        return pLocation;
      }

      public int[] getRoute(){
          return route;
      }

      public void moveTo(int location, double time, double newServiceTime) {
        this.location = location;
        this.arrivalTime = time;
        this.serviceTime = newServiceTime;
    }

    public double getSystemArrival(){
        return systemArrival;
    }

    public void setVqTime(double t){
        this.vqTime = t;
    }

    public double getVqTime(){
        return vqTime;
    }

    public int getCT(){
        return carType;
    }

    public void setSystemArrival(double t){
        this.systemArrival = t;
    }
}
