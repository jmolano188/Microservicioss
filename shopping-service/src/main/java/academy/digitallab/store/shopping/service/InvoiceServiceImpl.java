package academy.digitallab.store.shopping.service;

import academy.digitallab.store.shopping.entity.Invoice;
import academy.digitallab.store.shopping.repository.InvoiceItemRepository;
import academy.digitallab.store.shopping.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepository invoiceRepository;

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
        invoice.setStatus("CREATED");
        return invoiceRepository.save(invoice);
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
        return invoiceRepository.findById(id).orElse(null);
    }
}
