package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.OrderRequest;
import mr.demonid.web.client.links.CartServiceClient;
import mr.demonid.web.client.links.OrderServiceClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    OrderServiceClient orderServiceClient;
    CartServiceClient cartServiceClient;

    public void createOrder(OrderRequest orderRequest) {
        try {
            orderServiceClient.createOrder(orderRequest);
            cartServiceClient.clearCart();
        } catch (FeignException e) {
            System.out.println("Error: " + e.contentUTF8());
        }
    }
}
