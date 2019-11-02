package com.cuongnm.shoppingcartapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cuongnm.shoppingcartapp.entity.Account;
import com.cuongnm.shoppingcartapp.model.AccountInfo;
import com.cuongnm.shoppingcartapp.pagination.PaginationResult;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Account findAccount(String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, userName);
	}

	public PaginationResult<AccountInfo> listAccountInfo(int page, int maxResult, int maxNavigationPage) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountInfo> query = null;

		String sql = "Select new " + AccountInfo.class.getName()//
				+ "(acc.userName, acc.userRole, acc.active, acc.fullName, acc.email, acc.address) from " + Account.class.getName() + " acc "//
				+ " order by acc.userName desc";
		query = session.createQuery(sql, AccountInfo.class);

		return new PaginationResult<AccountInfo>(query, page, maxResult, maxNavigationPage);
	}
	

}