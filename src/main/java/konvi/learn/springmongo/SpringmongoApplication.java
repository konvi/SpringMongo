package konvi.learn.springmongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootApplication
public class SpringmongoApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringmongoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}

		// MongoTemplate section
		Customer user = new Customer("A", "B");
		mongoTemplate.insert(user, "customer");

		user = mongoTemplate.findOne(Query.query(Criteria.where("firstName").is("A")), Customer.class);
		user.lastName = "C";
		mongoTemplate.save(user, "customer");

		Query query = new Query();
		query.addCriteria(Criteria.where("firstName").is("Alice"));
		Update update = new Update();
		update.set("firstName", "AliceMarie");
		mongoTemplate.updateMulti(query, update, Customer.class);
	}

}
