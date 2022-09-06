package academy.digitallab.store.shopping.client;

import academy.digitallab.store.shopping.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient(name = "customer-service")
@RequestMapping(value = "/customers")
public interface CustomerClient {
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id")Long id);
}
