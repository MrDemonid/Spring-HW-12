package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.links.CartServiceClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    CartServiceClient cartServiceClient;

    public Integer getProductCount() {
        try {
            return cartServiceClient.getItemQuantity().getBody();
        } catch (FeignException e) {
            System.out.println("getProductCount: " + e.contentUTF8());
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

    public List<CartItem> getItems() {
        try {
            return cartServiceClient.getItems().getBody();
        } catch (FeignException e) {
            System.out.println("getItems: " + e.contentUTF8());
            return Collections.emptyList();
        }
    }
}
