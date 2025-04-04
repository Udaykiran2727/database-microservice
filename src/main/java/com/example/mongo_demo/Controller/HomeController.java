package com.example.mongo_demo.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mongo_demo.model.Cart;
import com.example.mongo_demo.model.Course;
import com.example.mongo_demo.repository.CartRepo;
import com.example.mongo_demo.repository.CourseRepo;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RestController
public class HomeController {
	@Autowired
	CourseRepo repo;
	
	@Autowired
	CartRepo cartrepo;
	
	@Autowired
	MongoClient client;
	
	@Autowired
	MongoConverter convert;
	
	@Value("${spring.data.mongodb.database}")
	private String database;
	
	@Value("${mongodbdata.collection}")
	private String collection;
	
	@GetMapping("/allCourse")
	public List<Course> getCourse() {
		
		return repo.findAll();
		
	}
	
	@PostMapping("/addCourse")
    public Course createCourse(@RequestBody Course course) {
        return repo.save(course);
    }

	@PutMapping("/editCourse")
	public Course editCourse(@RequestBody Course course) {
		return repo.save(course);
	}
	
    @DeleteMapping("/{id}")
    public void deleteCourseuct(@PathVariable String id) {
        repo.deleteById(id);
    }
    
    @GetMapping("/search/{text}")
    public List<Course> searchCourse(@PathVariable String text){
    	List<Course> k=new ArrayList<Course>();
    	
    	MongoDatabase database = client.getDatabase(this.database);
    	MongoCollection<Document> collection = database.getCollection(this.collection);
    	AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
    	    new Document("index", "default")
    	            .append("text", 
    	    new Document("query", text)
    	                .append("path", Arrays.asList("title", "details", "category","available"))))));
    	
    	
    	result.forEach(doc-> k.add(convert.read(Course.class, doc)));
		return k;
    	
    }
    
    @PostMapping("/addtoCart/{user_id}/{prod_id}")
    public void addtoCart(@PathVariable String user_id,@PathVariable String prod_id){
    	Cart cart=new Cart();
    	cart.productId=prod_id;
    	cart.userId=user_id;
    	cart.status="no";
    	
    	cartrepo.save(cart);
    }

    @GetMapping("/fetchCart/{user_id}")
    public List<Cart> searchCart(@PathVariable String user_id) {
    	String status="no";
    	
    	return cartrepo.findByUserIdAndStatus(user_id, status);
    	
    }
    
    @PutMapping("/fetchCart/{cart_id}")
    public void buyCart(@PathVariable String cart_id) {
    	Optional<Cart> optionalcart=cartrepo.findById(cart_id);
    	if (optionalcart.isPresent()) {
    		Cart cart=optionalcart.get();
    		cart.setStatus("yes");
    		cartrepo.save(cart);
    	}
    	else {
            throw new RuntimeException("Cart not found with id: " + cart_id);
        }
    }
    
    @GetMapping("/fetchMylearning/{user_id}")
    public List<Course> mylearning(@PathVariable String user_id){
    	String status="yes";
    	List<Cart> cart=cartrepo.findByUserIdAndStatus(user_id, status);
    	
    	List<String> courseIds = cart.stream()
                .map(Cart::getProduct_id)
                .collect(Collectors.toList());
    	
		return repo.findAllById(courseIds);
    	
    }

}
