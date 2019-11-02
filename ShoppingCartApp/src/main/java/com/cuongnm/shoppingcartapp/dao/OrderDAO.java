package com.cuongnm.shoppingcartapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cuongnm.shoppingcartapp.entity.Account;
import com.cuongnm.shoppingcartapp.entity.Order;
import com.cuongnm.shoppingcartapp.entity.OrderDetail;
import com.cuongnm.shoppingcartapp.entity.Product;
import com.cuongnm.shoppingcartapp.model.CartInfo;
import com.cuongnm.shoppingcartapp.model.CartLineInfo;
import com.cuongnm.shoppingcartapp.model.CustomerInfo;
import com.cuongnm.shoppingcartapp.model.OrderDetailInfo;
import com.cuongnm.shoppingcartapp.model.OrderInfo;
import com.cuongnm.shoppingcartapp.pagination.PaginationResult;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Transactional
@Repository
public class OrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private ProductDAO productDAO;

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo cartInfo, String userName) {
		Session session = this.sessionFactory.getCurrentSession();

		int orderNum = this.getMaxOrderNum() + 1;
		Order order = new Order();

		order.setId(UUID.randomUUID().toString());
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhone());
		order.setCustomerAddress(customerInfo.getAddress());
		Account account = null;
		if (userName != null) {
			account = accountDAO.findAccount(userName);
		}
		if (account != null) {
			order.setAccount(account);
		}
		session.persist(order);

		List<CartLineInfo> lines = cartInfo.getCartLines();

		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setId(UUID.randomUUID().toString());
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getProductInfo().getPrice());
			detail.setQuanity(line.getQuantity());

			String code = line.getProductInfo().getCode();
			Product product = this.productDAO.findProduct(code);
			detail.setProduct(product);

			session.persist(detail);
		}

		// Order Number!
		cartInfo.setOrderNum(orderNum);
		// Flush
		session.flush();
	}

	// @page = 1, 2, ...
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage, String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "";
		Query<OrderInfo> query = null;
		if (userName != null) {
			sql = "Select new " + OrderInfo.class.getName()//
					+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
					+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.account.userName, ord.account.fullName, ord.account.email,ord.account.address) "
					+ " from " + Order.class.getName() + " ord " + " where ord.account.userName = :userName ";
			query = session.createQuery(sql, OrderInfo.class);
			query.setParameter("userName", userName);
		} else {
			sql = "Select new " + OrderInfo.class.getName()//
					+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
					+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.account.userName) "
					+ " from " + Order.class.getName() + " ord "
					+ " order by ord.orderNum desc";
			query = session.createQuery(sql, OrderInfo.class);
		}
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public Order findOrder(String orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Order.class, orderId);
	}

	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		if(order.getAccount() != null) {
		return new OrderInfo(order.getId(), order.getOrderDate(), //
				order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
				order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone(),
				order.getAccount().getUserName(), order.getAccount().getFullName(), order.getAccount().getEmail(), order.getAccount().getAddress());
		}else {
			return new OrderInfo(order.getId(), order.getOrderDate(), //
					order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
					order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone(), null);
		}
	}

	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
		query.setParameter("orderId", orderId);

		return query.getResultList();
	}

}
