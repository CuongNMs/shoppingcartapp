package com.cuongnm.shoppingcartapp.form;

import org.springframework.web.multipart.MultipartFile;

import com.cuongnm.shoppingcartapp.entity.Product;

public class ProductForm {
	private String code;
	private String name;
	private String type;
	private double price;
	private String detail;
	private boolean newProduct = false;

	// Upload file.
	private MultipartFile fileData;

	public ProductForm() {
		this.newProduct = true;
	}

	public ProductForm(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.type = product.getType();
		this.detail = product.getDetail();
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

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
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