package kitchenpos.application;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        menuProductRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
    }
}
