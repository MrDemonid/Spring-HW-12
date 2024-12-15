package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.links.CartServiceClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {

    CartServiceClient cartServiceClient;

    public Integer getProductCount() {
        try {
            Integer res = cartServiceClient.getItemQuantity().getBody();
            System.out.println("getProductCount: " + res);
            return res;
        } catch (FeignException e) {
            return 0;
        }
    }

    public void addToCart(String productId, Integer quantity) {
        try {
            cartServiceClient.addItem(productId, quantity);
        } catch (FeignException e) {
            System.out.println("addToCart: " + e.contentUTF8());
        }
    }
}
