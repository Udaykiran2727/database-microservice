package com.example.mongo_demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cart")
public class Cart {

	@Id
	public String cartId;
	
	public String userId;
	public String productId;
	public String status;

	public Cart() {
		super();
		}
	
	public Cart(String user_id, String product_id, String bought,String cart_id) {
		super();
		this.userId = user_id;
		this.productId = product_id;
		this.status = bought;
		this.cartId=cart_id;
	}
	public String getCart_id() {
		return cartId;
	}

	public void setCart_id(String cart_id) {
		this.cartId = cart_id;
	}

	public String getUser_id() {
		return userId;
	}
	public void setUser_id(String user_id) {
		this.userId = user_id;
	}
	public String getProduct_id() {
		return productId;
	}
	public void setProduct_id(String product_id) {
		this.productId = product_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
