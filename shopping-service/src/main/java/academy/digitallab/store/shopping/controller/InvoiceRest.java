package academy.digitallab.store.shopping.controller;
import academy.digitallab.store.shopping.entity.Invoice;
import academy.digitallab.store.shopping.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/invoices")
public class InvoiceRest {
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping
    public ResponseEntity<List<Invoice>>listAllInvoice(){
        List<Invoice> invoices=invoiceService.findInvoiceAll();
        if (invoices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }
    @CircuitBreaker(name = "getInvoiceCB", fallbackMethod ="fallbackgetInvoice" )
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id")Long id){
        log.info("fetching invoice with id {}",id);
        Invoice invoice=invoiceService.getInvoice(id);
        if (invoice==null){
            log.error("Invoice with id {} not found",id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }
    @CircuitBreaker(name = "createInvoiceCB", fallbackMethod = "fallbackcreateInvoice")
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult result){
        log.info("Creating invoice :{}",invoice);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
        }
        Invoice invoice1=invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice1);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("id")Long id,@RequestBody Invoice invoice){
        log.info("Updating invoice with id: {}",id);
        invoice.setId(id);
        Invoice invoice1=invoiceService.updateInvoice(invoice);
        if (invoice1== null){
            log.error("Unable to update.Invoice with id {}",id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice1);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Invoice with id {}", id);

        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            log.error("Unable to delete. Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        invoice.setStatus("DELETE");
        invoice = invoiceService.deleteInvoice(invoice);
        return ResponseEntity.ok(invoice);
    }
    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
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
    private ResponseEntity<Invoice> createInvoiceCB(@Valid @RequestBody Invoice invoice, BindingResult result,RuntimeException e){
        return new ResponseEntity("No se ha podido actualizar el stock",HttpStatus.OK);
    }
    private ResponseEntity<Invoice> getInvoiceCB(@PathVariable("id")Long id, RuntimeException e){
    return new ResponseEntity("No se ha podido acceder a la informacion del usuario",HttpStatus.OK);
    }
}
