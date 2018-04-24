//package Week4ANoNetwork;

import java.util.ArrayList;

public class Queue {

    /* List of customers in the queue */
    protected ArrayList<Customer> customers;
    /* Number of servers for this queue */
    protected int nrOfServers;

    /**
     * Constructs an empty single-server Queue
     */
    public Queue() {
        this(1);
    }

    /**
     * Constructs an empty Queue with the specified number of servers
     */
    public Queue(int nrOfServers) {
        customers = new ArrayList<Customer>();
        this.nrOfServers = nrOfServers;
    }

    /**
     * Returns the number of customers in the queue (including the person in
     * service)
     *
     * @return the number of customers
     */
    public int getSize() {
        return customers.size();
    }

    /**
     * Adds a customer to this queue
     *
     * @param c the new customer
     */
    public void addCustomer(Customer c) {
        customers.add(c);
    }

    /**
     * Returns the first customer in the queue.
     *
     * @return the first customer
     */
    public Customer getFirstCustomer() {
        return customers.get(0);
    }

    /**
     * Removes the first customer from the queue at time <i>t</i>
     *
     * @param t the time that the customer is removed
     * @return the customer that is removed from the queue
     */
    public Customer removeFirstCustomer() {
        return customers.remove(0);
    }

    /**
     * Removes the specified customer.
     *
     * @param customer the customer that should be removed
     */
    public void removeCustomer(Customer c) {
        customers.remove(c);
    }

    /**
     * Returns the position of the customer in this queue, or -1 if the customer
     * cannot be found.
     *
     * @param customer the customer whose position should be determined.
     * @return the index of the customer in the queue, or -1 if not found.
     */
    public int getCustomerPosition(Customer c) {
        return customers.indexOf(c);
    }

    /**
     * Returns the customer at the specified position
     *
     * @param position the position in the queue
     * @return the customer at the specified position
     */
    public Customer getCustomerAtPosition(int position) {
        return customers.get(position);
    }

    /**
     * Returns the number of servers for this queue
     *
     * @return the number of servers for this queue
     */
    public int getNumberOfServers() {
        return nrOfServers;
    }
}
