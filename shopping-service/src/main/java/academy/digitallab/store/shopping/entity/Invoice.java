package academy.digitallab.store.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number_invoice")
    private String numberInvoice;
    private String description;
    @Column(name = "customer_id")
    private Long CustomerId;
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date creatAt;
   @Valid

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
   @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
   @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> items;
    private String state;

    public Invoice(){
        items=new ArrayList<>();
    }
    @PrePersist
    public void prePersist(){this.creatAt=new Date();}


}
