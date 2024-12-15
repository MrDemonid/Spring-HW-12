package mr.demonid.web.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartItem {
    private String userId;
    private String productId;
    private int quantity;
}
