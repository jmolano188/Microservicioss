package academy.digitallab.store.shopping.service;

import academy.digitallab.store.shopping.client.CustomerClient;
import academy.digitallab.store.shopping.client.ProductClient;
import academy.digitallab.store.shopping.entity.Invoice;
import academy.digitallab.store.shopping.entity.InvoiceItem;
import academy.digitallab.store.shopping.model.Customer;
import academy.digitallab.store.shopping.model.Product;
import academy.digitallab.store.shopping.repository.InvoiceItemRepository;
import academy.digitallab.store.shopping.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoice1=invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoice1!=null){
            return invoice1;
        }
        invoice1.setStatus("CREATED");
        invoice1=invoiceRepository.save(invoice);
        invoice1.getItems().forEach( invoiceItem -> {
            productClient.updateStockProduct( invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
        return invoice1;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoice1=getInvoice(invoice.getId());
        if (invoice1==null){
            return null;
        }
        invoice1.setCustomerId(invoice.getCustomerId());
        invoice1.setDescription(invoice.getDescription());
        invoice1.setNumberInvoice(invoice.getNumberInvoice());
        invoice1.getItems().clear();
        invoice1.setItems(invoice.getItems());
        return invoiceRepository.save(invoice1);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoice1=getInvoice(invoice.getId());
        if (invoice1==null){
            return  null;
        }
        invoice1.setStatus("DELETE");
        return invoiceRepository.save(invoice1);
    }

    @Override
    public Invoice getInvoice(Long id) {

       Invoice invoice= invoiceRepository.findById(id).orElse(null);
       if (invoice!=null){
           Customer customer=customerClient.getCustomer(invoice.getCustomerId()).getBody();
            invoice.setCustomer(customer);
            List<InvoiceItem>listItems=invoice.getItems().stream().map(invoiceItem -> {
                        Product product=productClient.getProduct(invoiceItem.getProductId()).getBody();
                        invoiceItem.setProduct(product);
                        return invoiceItem;
                    }).collect(Collectors.toList());
           invoice.setItems(listItems);
       }
return invoice;
    }
}
