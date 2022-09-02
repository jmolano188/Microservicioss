package academy.digitallab.store.product.service;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product product1=getProductById(product.getId());
        if(product1==null){
            return null;
        }
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setCategory(product.getCategory());
        product1.setPrice(product.getPrice());
        return productRepository.save(product1);

    }

    @Override
    public Product deleteProduct(Long id) {
        Product product1=getProductById(id);
        if(product1==null){
            return null;
        }
        product1.setStatus("DELETED");
        return productRepository.save(product1);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product product=getProductById(id);
        if(product== null) {
            return null;
        }
        Double stock=product.getStock()+quantity;
        product.setStock(stock);
       return productRepository.save(product);
    }
}
