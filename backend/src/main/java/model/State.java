package model;

public class State {
    private Customer customer;
    private Order order;
    private int failedClicks;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getFailedClicks() {
        return failedClicks;
    }

    public void incrementFailedClicks() {
        this.failedClicks++;
    }

    public void decrementFailedClicks() {
        this.failedClicks--;
    }
}
