package com.cuongnm.shoppingcartapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
 
    private static final long serialVersionUID = -2054386655979281969L;
 
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_USER = "USER";
 
    @Id
    @Column(name = "User_Name", length = 20, nullable = false)
    private String userName;
 
    @Column(name = "Encryted_Password", length = 128, nullable = false)
    private String encrytedPassword;
 
    @Column(name = "Active", length = 1, nullable = false)
    private int active;
 
    @Column(name = "User_Role", length = 20, nullable = false)
    private String userRole;
    
    @Column(name = "Full_Name", length=20, nullable = false)
    private String fullName;
    
    @Column(name = "Email", length=20, nullable = false)
    private String email;
    
    @Column(name = "Address", length=20, nullable = false)
    private String address;
    
    
    
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getEncrytedPassword() {
        return encrytedPassword;
    }
 
    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }
 
    public int getActive() {
        return active;
    }
 
    public void setActive(int active) {
        this.active = active;
    }
 
    public String getUserRole() {
        return userRole;
    }
 
    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

	@Override
    public String toString() {
        return "[" + this.userName + "," + this.encrytedPassword + "," + this.userRole + "]";
    }
 
}