package academy.digitallab.store.shopping.model;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String numberId;
    private String firstname;
    private String lastName;
    private String email;
    private String photoUrl;
    private Region region;
    private String status;
}
