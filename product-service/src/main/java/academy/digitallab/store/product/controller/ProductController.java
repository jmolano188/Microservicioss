package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
