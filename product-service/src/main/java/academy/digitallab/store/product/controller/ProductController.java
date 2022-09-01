package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.controller.entity.Category;
import academy.digitallab.store.product.controller.entity.Product;
import academy.digitallab.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public ResponseEntity <List<Product>> listProudct(@RequestParam(name = "categoryId",required = false) Long categoryId){
        List<Product>products = new ArrayList<>();
        if (categoryId==null){
            products=productService.listAllProduct();
            if(products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else {
            products=productService.findByCategory(Category.builder().id(categoryId).build());
            if(products.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }


        return ResponseEntity.ok(products);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id")Long id){
      Product product=productService.getProductById(id);
    if(product==null){
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(product);
    }
@PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
       Product productCreate=productService.createProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product>updateProduct(@PathVariable("id") Long id,@RequestBody Product product){
        product.setId(id);
        Product productDB=productService.updateProduct(product);
        if(productDB==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product>deleteProduct(@PathVariable("id")Long id){
        Product productDElete=productService.deleteProduct(id);
        if (productDElete==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDElete);
    }
    @GetMapping(value = "/{id}/stock")
    public ResponseEntity<Product>updateStockProduct(@PathVariable("id") Long id,@RequestParam(name = "quantity",required = true) Double quantity){
        Product product=productService.updateStock(id,quantity);
        if (product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
