package academy.digitalab.store.customer.service;

import academy.digitalab.store.customer.entity.Customer;
import academy.digitalab.store.customer.entity.Region;
import academy.digitalab.store.customer.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<Customer> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomerByRegion(Region region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customer1=customerRepository.findByNumberID(customer.getNumberID());
        if(customer1!=null){
            return customer1;
        }
        customer.setState("CREATED");
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customer1=customerRepository.findByNumberID(customer.getNumberID());
        if(customer1==null){
            return null;
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer deleteCustomer(Long id) {
        Customer customer=getCustomer(id);
        if(customer==null){
            return null;
        }
        customer.setState("DELETED");
        return customerRepository.save(customer);
    }
}
