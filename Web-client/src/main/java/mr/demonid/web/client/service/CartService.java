package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.links.CartServiceClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    CartServiceClient cartServiceClient;
    CatalogService catalogService;

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

    /**
     * Возвращает список товаров в корзине.
     */
    public List<CartItem> getItems() {
        try {
            List<CartItem> items = cartServiceClient.getItems().getBody();
            if (items != null) {
                items.forEach(this::fillItem);
            }
            return items;
        } catch (FeignException e) {
            System.out.println("getItems: " + e.contentUTF8());
            return Collections.emptyList();
        }
    }

    /**
     * Дополняет данные о товаре в корзине, получая
     * наименование товара и вычисляя его стоимость.
     */
    private void fillItem(CartItem item) {
        try {
            ProductInfo product = catalogService.getProductById(Long.parseLong(item.getProductId()));
            item.setProductName(product.getName());
            item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            System.out.println("    -- item: " + item);
        } catch (Exception e) {
            System.out.println("fillItem: " + e.getMessage());
            item.setProductName("Unknown");
            item.setPrice(BigDecimal.ZERO);
            item.setQuantity(0);
        }

    }

}
