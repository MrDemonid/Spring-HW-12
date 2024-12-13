package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public void createCategory(String name) {
        if (categoryRepository.findByName(name) == null) {
            categoryRepository.save(new ProductCategoryEntity(name));
        }
    }

}