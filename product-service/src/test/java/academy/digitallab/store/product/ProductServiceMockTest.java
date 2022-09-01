package academy.digitallab.store.product;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import academy.digitallab.store.product.service.ProductService;
import academy.digitallab.store.product.service.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {
    @Mock
    private ProductRepository productRepository;
    private ProductService productService;
@BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        productService=new ProductServiceImpl(productRepository);
        Product computer= Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .description("Descripcions")
                .stock(Double.parseDouble("5"))
                .price(Double.parseDouble("1240.99"))
                .status("created")
                .createAt(new Date()).build();
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(computer));
    Mockito.when(productRepository.save(computer)).thenReturn(computer);
}
@Test
public void whenValidGetId_ThenReturnProduct(){
    Product foud=productService.getProductById(1L);
    Assertions.assertThat(foud.getName()).isEqualTo("computer");
}
@Test
public void whenValidStock_ThenReturnNewStock(){
    Product newStock= productService.updateStock(1L,Double.parseDouble("8"));
    Assertions.assertThat(newStock.getStock()).isEqualTo(13);
}
}
