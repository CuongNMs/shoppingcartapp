package com.cuongnm.shoppingcartapp.model;

import com.cuongnm.shoppingcartapp.entity.Product;

public class ProductInfo {
	private String code;
	private String name;
	private double price;
	private String type;
	private String detail;
	public ProductInfo() {
	}

	public ProductInfo(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.type = product.getType();
	}

	// Using in JPA/Hibernate query
	public ProductInfo(String code, String name, double price, String detail) {
		this.code = code;
		this.name = name;
		this.price = price;
		this.detail = detail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}