package com.cuongnm.shoppingcartapp.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cuongnm.shoppingcartapp.entity.Account;
import com.cuongnm.shoppingcartapp.form.AccountForm;
import com.cuongnm.shoppingcartapp.model.AccountInfo;
import com.cuongnm.shoppingcartapp.pagination.PaginationResult;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Account findAccount(String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, userName);
	}

	public PaginationResult<AccountInfo> listAccountInfo(int page, int maxResult, int maxNavigationPage) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountInfo> query = null;

		String sql = "Select new " + AccountInfo.class.getName()//
				+ "(acc.userName, acc.userRole, acc.active, acc.fullName, acc.email, acc.address) from "
				+ Account.class.getName() + " acc "//
				+ " order by acc.userName desc";
		query = session.createQuery(sql, AccountInfo.class);

		return new PaginationResult<AccountInfo>(query, page, maxResult, maxNavigationPage);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public PaginationResult<AccountInfo> setActiveAccount(Account account, int page, int maxResult,
			int maxNavigationPage, int value) {
		Session session = this.sessionFactory.getCurrentSession();
		Account tmpAccount = this.findAccount(account.getUserName());
		tmpAccount.setActive(value);
		session.persist(tmpAccount);
		session.flush();
		return listAccountInfo(page, maxResult, maxNavigationPage);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(AccountForm accountForm) {

		Session session = this.sessionFactory.getCurrentSession();
		String userName = accountForm.getUserName();

		Account account = null;

		boolean isNew = false;
		if (userName != null) {
			account = this.findAccount(userName);
		}
		if (account == null) {
			isNew = true;
			account = new Account();
			account.setUserRole("ROLE_USER");
			account.setActive(1);
		}
		account.setUserName(userName);
		account.setEncrytedPassword(passwordEncoder.encode(accountForm.getPassword()));
		account.setFullName(accountForm.getFullName());
		account.setEmail(accountForm.getEmail());
		account.setAddress(accountForm.getAddress());
		
		if (isNew) {
			session.persist(account);
		}
		// If error in DB, Exceptions will be thrown out immediately
		session.flush();
	}

}