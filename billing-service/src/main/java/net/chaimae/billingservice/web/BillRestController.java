package net.chaimae.billingservice.web;

import net.chaimae.billingservice.entities.Bill;
import net.chaimae.billingservice.feign.CustomerRestClient;
import net.chaimae.billingservice.feign.ProductRestClient;
import net.chaimae.billingservice.repository.BillRepository;
import net.chaimae.billingservice.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private CustomerRestClient customerRestClient;

    @Autowired
    private ProductRestClient productRestClient;
    @GetMapping(path = "/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;
    }
}