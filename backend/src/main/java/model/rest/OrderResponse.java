package model.rest;

public class OrderResponse {
    private final String orderUrl;

    public OrderResponse(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getOrderUrl() {
        return orderUrl;
    }
}
