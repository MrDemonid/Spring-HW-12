package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.Product;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByCategory(ProductCategoryEntity category);
}
