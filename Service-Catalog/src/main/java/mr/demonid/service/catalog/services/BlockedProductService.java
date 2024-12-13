package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.BlockedProduct;
import mr.demonid.service.catalog.repositories.BlockedProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BlockedProductService {

    private BlockedProductRepository blockedRepository;

    public void reserve(UUID orderId, long productId, int quantity) {
        blockedRepository.save(new BlockedProduct(orderId, productId, quantity));
    }

    public BlockedProduct unblock(UUID orderId) {
        BlockedProduct blockedProduct = blockedRepository.findById(orderId).orElse(null);
        blockedRepository.deleteById(orderId);
        return blockedProduct;
    }
}
