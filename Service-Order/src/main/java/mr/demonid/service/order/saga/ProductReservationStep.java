package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.service.order.dto.ProductReservationRequest;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.CatalogServiceClient;

/**
 * Шаг: резервирование товара на складе.
 */
@AllArgsConstructor
public class ProductReservationStep implements SagaStep<SagaContext> {

    CatalogServiceClient catalogServiceClient;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        ProductReservationRequest request = new ProductReservationRequest(context.getOrderId(), context.getUserId(), context.getProductId(), context.getQuantity(), context.getPrice());
        try {
            catalogServiceClient.reserve(request);
        } catch (FeignException e) {
            throw new SagaStepException(e.getMessage());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        try {
            catalogServiceClient.unblock(context.getOrderId());
        } catch (FeignException ignored) {}
    }


}
