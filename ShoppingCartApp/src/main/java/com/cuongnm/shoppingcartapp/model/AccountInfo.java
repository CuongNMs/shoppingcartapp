package com.cuongnm.shoppingcartapp.model;


public class AccountInfo {
	private String userName;
    private String userRole;
    private boolean active;
    private String fullName;
    private String email;
    private String address;
    
    public AccountInfo() {
 
    }
 
    public AccountInfo(String userName, String userRole, boolean active, String fullName, String email, String address) {
        this.userName = userName;
        this.userRole = userRole;
        this.active = active;
        this.fullName = fullName;
        this.email = email;
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String role) {
		this.userRole = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
 
    
}
