package academy.digitalab.store.customer.service;

import academy.digitalab.store.customer.entity.Customer;
import academy.digitalab.store.customer.entity.Region;

import java.util.List;

public interface CustomerService {
    public List<Customer> findCustomerAll();
    public List<Customer> findCustomerByRegion(Region region);
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer getCustomer(Long id);
    public Customer deleteCustomer(Long id);
}
