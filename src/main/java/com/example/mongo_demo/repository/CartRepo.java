package com.example.mongo_demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mongo_demo.model.Cart;

@Repository
public interface CartRepo extends MongoRepository<Cart,String>{
	
	List<Cart> findByUserIdAndStatus(String userId, String status);

}
