package academy.digitalab.store.customer.controller;

import academy.digitalab.store.customer.entity.Customer;
import academy.digitalab.store.customer.entity.Region;
import academy.digitalab.store.customer.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerRest {
    @Autowired
    private CustomerService customerService;
    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId", required = false)Long regionid){
    List<Customer>customers=new ArrayList<>();
    if (regionid==null){
        customers=customerService.findCustomerAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    }else{
        Region region=new Region();
        region.setId(regionid);
        customers=customerService.findCustomerByRegion(region);
        if (customers==null){
            log.error("customer with id {} not found",regionid);
            return ResponseEntity.notFound().build();
        }
    }
    return ResponseEntity.ok(customers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Customer>getCustomer(@PathVariable("id")Long id){
     log.info("Fetching Customer with id{}",id);
     Customer customer=customerService.getCustomer(id);
     if(customer==null){
         log.error("Customer with id {} not found",id);
         return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok(customer);
    }
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        log.info("Creating Customer: {}", customer);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Customer customer1=customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer1);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id")Long id, @RequestBody Customer customer){
        Customer customer1=customerService.getCustomer(id);
        log.info("updating Customer with id {}",id);
        if (customer1==null){
            log.error("unable to update, Customer with id {} not found",id);
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customer1=customerService.updateCustomer(customer);
        return ResponseEntity.ok(customer1);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id")Long id){
        log.info("Fetching & delete. Customer with id {}",id);
        Customer customer=customerService.getCustomer(id);
        if (customer==null){
            log.error("Unable to delete. Customer with id {} not found",id);
            return ResponseEntity.notFound().build();
        }
        customer=customerService.deleteCustomer(id);
        return ResponseEntity.ok(customer);
    }
        private String formatMessage( BindingResult result){
            List<Map<String,String>> errors = result.getFieldErrors().stream()
                    .map(err ->{
                        Map<String,String>  error =  new HashMap<>();
                        error.put(err.getField(), err.getDefaultMessage());
                        return error;

                    }).collect(Collectors.toList());
            ErrorMessage errorMessage = ErrorMessage.builder()
                    .code("01")
                    .messages(errors).build();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString="";
            try {
                jsonString = mapper.writeValueAsString(errorMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return jsonString;
        }
}
