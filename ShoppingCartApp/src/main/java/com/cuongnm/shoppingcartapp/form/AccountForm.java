package com.cuongnm.shoppingcartapp.form;

import com.cuongnm.shoppingcartapp.entity.Account;

public class AccountForm {

	private String userName;
	private String password;
	private String fullName;
	private String email;
	private String address;

	public AccountForm() {

	}

	public AccountForm(Account account) {
		if (account != null) {
			this.setUserName(account.getUserName());
			this.setFullName(account.getFullName());
			this.setPassword(account.getEncrytedPassword());
			this.setEmail(account.getEmail());
			this.setAddress(account.getAddress());
		}
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
