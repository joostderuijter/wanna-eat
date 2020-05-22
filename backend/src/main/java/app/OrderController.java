package app;

import model.OrderRequest;
import model.State;
import model.rest.OrderResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.OrderMapper;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public OrderResponse placeOrder(OrderRequest request) {
        try {
            return new OrderResponse(orderService.placeOrderForCustomer(OrderMapper.mapOrderRequestToCustomer(request)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OrderResponse("");
    }
}
