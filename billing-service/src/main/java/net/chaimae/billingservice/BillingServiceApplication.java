package net.chaimae.billingservice;

import net.chaimae.billingservice.feign.CustomerServiceRestClient;
import net.chaimae.billingservice.feign.InventoryServiceRestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.chaimae.billingservice.entities.Bill;
import net.chaimae.billingservice.entities.ProductItem;

import net.chaimae.billingservice.model.Customer;
import net.chaimae.billingservice.model.Product;
import net.chaimae.billingservice.repository.BillRepository;
import net.chaimae.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(BillRepository  billRepository,
										ProductItemRepository productItemRepository,
										CustomerServiceRestClient customerRestClient,
										InventoryServiceRestClient	productRestClient){

		return args -> {
			Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
			Collection<Product> products = productRestClient.getAllProducts().getContent();

			customers.forEach(customer -> {
				Bill bill = Bill.builder()
						.billingDate(new Date())
						.customerId(customer.getId())
						.build();
				billRepository.save(bill);
				products.forEach(product -> {
					ProductItem productItem = ProductItem.builder()
							.bill(bill)
							.productId(product.getId())
							.quantity(1+new Random().nextInt(10))
							.unitPrice(product.getPrice())
							.build();
					productItemRepository.save(productItem);
				});
			});
		};
	}

}
