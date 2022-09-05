package kitchenpos.application;

import static kitchenpos.domain.MenuFixture.Menu;
import static kitchenpos.domain.MenuFixture.MenuWithUUIDAndMenuGroup;
import static kitchenpos.domain.MenuFixture.MenuWithoutMenuProducts;
import static kitchenpos.domain.MenuGroupFixture.MenuGroupWithUUID;
import static kitchenpos.domain.MenuProductFixture.MenuProduct;
import static kitchenpos.domain.MenuProductFixture.MenuProductWithProduct;
import static kitchenpos.domain.ProductFixture.Product;
import static kitchenpos.domain.ProductFixture.ProductWithUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("새로운 메뉴를 생성할 수 있다.")
    @Nested
    class Create {

        private MenuGroup 세트메뉴;
        private MenuProduct 햄버거_메뉴상품;
        private MenuProduct 콜라_메뉴상품;

        @BeforeEach
        void setUp() {
            Product 햄버거 = productRepository.save(ProductWithUUID("햄버거", 15_000));
            Product 콜라 = productRepository.save(ProductWithUUID("콜라", 2_000));
            햄버거_메뉴상품 = MenuProduct(햄버거.getId(), 1);
            콜라_메뉴상품 = MenuProduct(콜라.getId(), 1);
            세트메뉴 = menuGroupRepository.save(MenuGroupWithUUID("세트메뉴"));
        }

        @DisplayName("성공")
        @Test
        void create() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when
            Menu savedMenu = menuService.create(request);

            // then
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo("햄버거 + 콜라 세트메뉴");
            assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(17_000).setScale(2));
            assertThat(savedMenu.getMenuGroup()).usingRecursiveComparison()
                .isEqualTo(세트메뉴);
            assertThat(savedMenu.getMenuProducts()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                "seq",
                "product",
                "productId"
            ).containsExactly(햄버거_메뉴상품, 콜라_메뉴상품);
        }

        @DisplayName("메뉴 가격은 null 일 수 없다.")
        @Test
        void priceNullException() {
            // given
            Menu request = new Menu();
            request.setName("햄버거 + 콜라 세트메뉴");
            request.setPrice(null);
            request.setMenuGroupId(세트메뉴.getId());
            request.setMenuProducts(List.of(햄버거_메뉴상품, 콜라_메뉴상품));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격은 음수 일 수 없다.")
        @Test
        void priceNegativeException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                -1,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격은 (상품의 금액 * 수량)의 총합보다 작거나 같아야한다.")
        @Test
        void priceInvalidException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                17_001,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴가 속할 메뉴그룹이 우선 존재해야 한다.")
        @Test
        void menuGroupNotFoundException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴에는 1개 이상의 상품이 포함되어야 한다.")
        @Test
        void menuProductEmptyException() {
            // given
            Menu request = MenuWithoutMenuProducts(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴.getId()
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 생성요청에 주어진 상품 종류와 실제 상품 종류가 일치해야한다.")
        void invalidMenuProductException() {
            // given
            MenuProduct 감자튀김 = MenuProduct(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                1
            );

            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품, 감자튀김
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 생성요청에 주어진 상품 종류와 실제 상품 종류가 일치해야한다.")
        void productQuantityNegativeException() {
            // given
            Product 감자튀김 = Product("감자튀김", 3_000);
            MenuProduct 감자튀김_메뉴상품 = MenuProduct(감자튀김.getId(), -1);

            Menu request = Menu(
                "햄버거 + 콜라 + 감자튀김 세트메뉴",
                20_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품, 감자튀김_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 이름은 null 일 수 없다.")
        void nameNullException() {
            // given
            Menu request = Menu(
                null,
                20_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 이름엔 욕설을 포함할 수 없다.")
        void containsProfanityException() {
            // given
            Menu request = Menu(
                "ass 햄버거 콜라 세트",
                20_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 가격을 변경할 수 있다.")
    @Nested
    class ChangePrice {

        private MenuGroup 세트메뉴;
        private MenuProduct 햄버거_메뉴상품;
        private MenuProduct 콜라_메뉴상품;
        private Menu 햄버거_콜라_세트메뉴;

        @BeforeEach
        void setUp() {
            Product 햄버거 = productRepository.save(ProductWithUUID("햄버거", 15_000));
            Product 콜라 = productRepository.save(ProductWithUUID("콜라", 2_000));
            햄버거_메뉴상품 = MenuProductWithProduct(햄버거, 1);
            콜라_메뉴상품 = MenuProductWithProduct(콜라, 1);
            세트메뉴 = menuGroupRepository.save(MenuGroupWithUUID("세트메뉴"));
            햄버거_콜라_세트메뉴 = menuRepository.save(MenuWithUUIDAndMenuGroup(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴,
                햄버거_메뉴상품, 콜라_메뉴상품
            ));
        }

        @DisplayName("성공")
        @Test
        void success() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                15_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when
            Menu result = menuService.changePrice(햄버거_콜라_세트메뉴.getId(), request);

            // then
            assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(15_000).setScale(2));
            assertThat(result).usingRecursiveComparison()
                .ignoringFields("price")
                .isEqualTo(햄버거_콜라_세트메뉴);
        }

        @DisplayName("변경할 메뉴가 우선 존재해야 한다.")
        @Test
        void menuNotFoundException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                15_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.changePrice(
                UUID.fromString("00000000-0000-0000-0000-000000000000"), request
            )).isExactlyInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("새로운 메뉴 가격은 null 일 수 없다.")
        @Test
        void priceNullException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                15_000,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );
            request.setPrice(null);

            // when, then
            assertThatThrownBy(() -> menuService.changePrice(
                햄버거_콜라_세트메뉴.getId(), request
            )).isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("새로운 메뉴 가격은 음수 일 수 없다.")
        @Test
        void priceNegativeException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                -1,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.changePrice(
                햄버거_콜라_세트메뉴.getId(), request
            )).isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("새로운 메뉴 가격은 (상품의 금액 * 수량)의 총합보다 작거나 같아야한다.")
        @Test
        void priceInvalidException() {
            // given
            Menu request = Menu(
                "햄버거 + 콜라 세트메뉴",
                17_001,
                세트메뉴.getId(),
                햄버거_메뉴상품, 콜라_메뉴상품
            );

            // when, then
            assertThatThrownBy(() -> menuService.changePrice(
                햄버거_콜라_세트메뉴.getId(), request
            )).isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴를 사용자에게 노출되도록 변경할 수 있다.")
    @Nested
    class Display {

        private Menu 햄버거_콜라_세트메뉴;

        @BeforeEach
        void setUp() {
            Product 햄버거 = productRepository.save(ProductWithUUID("햄버거", 15_000));
            Product 콜라 = productRepository.save(ProductWithUUID("콜라", 2_000));
            MenuProduct 햄버거_메뉴상품 = MenuProductWithProduct(햄버거, 1);
            MenuProduct 콜라_메뉴상품 = MenuProductWithProduct(콜라, 1);
            MenuGroup 세트메뉴 = menuGroupRepository.save(MenuGroupWithUUID("세트메뉴"));
            햄버거_콜라_세트메뉴 = menuRepository.save(MenuWithUUIDAndMenuGroup(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴,
                햄버거_메뉴상품, 콜라_메뉴상품
            ));
        }

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Menu result = menuService.display(햄버거_콜라_세트메뉴.getId());

            // then
            assertThat(result.isDisplayed()).isTrue();
        }

        @DisplayName("노출할 메뉴가 우선 존재해야 한다.")
        @Test
        void menuNotFoundException() {
            // when, then
            assertThatThrownBy(() -> menuService.display(
                UUID.fromString("00000000-0000-0000-0000-000000000000")
            )).isExactlyInstanceOf(NoSuchElementException.class);
        }
    }

    @DisplayName("메뉴를 사용자에게 노출되지 않도록 감출 수 있다.")
    @Nested
    class Hide {

        private Menu 햄버거_콜라_세트메뉴;

        @BeforeEach
        void setUp() {
            Product 햄버거 = productRepository.save(ProductWithUUID("햄버거", 15_000));
            Product 콜라 = productRepository.save(ProductWithUUID("콜라", 2_000));
            MenuProduct 햄버거_메뉴상품 = MenuProductWithProduct(햄버거, 1);
            MenuProduct 콜라_메뉴상품 = MenuProductWithProduct(콜라, 1);
            MenuGroup 세트메뉴 = menuGroupRepository.save(MenuGroupWithUUID("세트메뉴"));
            햄버거_콜라_세트메뉴 = menuRepository.save(MenuWithUUIDAndMenuGroup(
                "햄버거 + 콜라 세트메뉴",
                17_000,
                세트메뉴,
                햄버거_메뉴상품, 콜라_메뉴상품
            ));
        }

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Menu result = menuService.hide(햄버거_콜라_세트메뉴.getId());

            // then
            assertThat(result.isDisplayed()).isFalse();
        }

        @DisplayName("감출 메뉴가 우선 존재해야 한다.")
        @Test
        void menuNotFoundException() {
            // when, then
            assertThatThrownBy(() -> menuService.hide(
                UUID.fromString("00000000-0000-0000-0000-000000000000")
            )).isExactlyInstanceOf(NoSuchElementException.class);
        }
    }

    @DisplayName("전체 메뉴를 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        Product 햄버거 = productRepository.save(ProductWithUUID("햄버거", 15_000));
        Product 콜라 = productRepository.save(ProductWithUUID("콜라", 2_000));
        MenuProduct 햄버거_메뉴상품 = MenuProductWithProduct(햄버거, 1);
        MenuProduct 콜라_메뉴상품 = MenuProductWithProduct(콜라, 1);
        MenuGroup 세트메뉴 = menuGroupRepository.save(MenuGroupWithUUID("세트메뉴"));
        MenuGroup 단품메뉴 = menuGroupRepository.save(MenuGroupWithUUID("단품메뉴"));
        Menu 햄버거_콜라_세트메뉴 = menuRepository.save(MenuWithUUIDAndMenuGroup(
            "햄버거 + 콜라 세트메뉴",
            17_000,
            세트메뉴,
            햄버거_메뉴상품, 콜라_메뉴상품
        ));
        Menu 햄버거_단품메뉴 = menuRepository.save(MenuWithUUIDAndMenuGroup(
            "햄버거 단품메뉴",
            15_000,
            단품메뉴,
            햄버거_메뉴상품
        ));

        // when
        List<Menu> result = menuService.findAll();

        // then
        assertThat(result).usingRecursiveFieldByFieldElementComparatorIgnoringFields("menuProducts")
            .containsExactly(햄버거_콜라_세트메뉴, 햄버거_단품메뉴);
    }
}
