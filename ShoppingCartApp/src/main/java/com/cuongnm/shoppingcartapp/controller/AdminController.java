package com.cuongnm.shoppingcartapp.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cuongnm.shoppingcartapp.dao.AccountDAO;
import com.cuongnm.shoppingcartapp.dao.OrderDAO;
import com.cuongnm.shoppingcartapp.dao.ProductDAO;
import com.cuongnm.shoppingcartapp.entity.Account;
import com.cuongnm.shoppingcartapp.entity.Product;
import com.cuongnm.shoppingcartapp.form.ProductForm;
import com.cuongnm.shoppingcartapp.model.AccountInfo;
import com.cuongnm.shoppingcartapp.model.OrderDetailInfo;
import com.cuongnm.shoppingcartapp.model.OrderInfo;
import com.cuongnm.shoppingcartapp.pagination.PaginationResult;
import com.cuongnm.shoppingcartapp.validator.ProductFormValidator;

@Controller
@Transactional
public class AdminController {

	private UserDetails userDetails = null;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ProductFormValidator productFormValidator;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == ProductForm.class) {
			dataBinder.setValidator(productFormValidator);
		}
	}

	// GET: Show Login Page
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "loginAdmin";
	}

	@RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(Model model) {
		userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account account = accountDAO.findAccount(userDetails.getUsername().toString());
		model.addAttribute("userDetails", userDetails);
		model.addAttribute("userInformation", account);
		return "accountInfoUser";
	}

	@RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
	public String orderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE, null);

		model.addAttribute("paginationResult", paginationResult);
		return "orderList";
	}

	// ---------------------------------------------

	@RequestMapping(value = { "/admin/userList" }, method = RequestMethod.GET)
	public String userList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<AccountInfo> paginationResult //
				= accountDAO.listAccountInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationResult", paginationResult);
		return "userList";
	}

	// ----------------------------------------------

	// GET: Show product.
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.GET)
	public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		ProductForm productForm = null;

		if (code != null && code.length() > 0) {
			Product product = productDAO.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		if (productForm == null) {
			productForm = new ProductForm();
			productForm.setNewProduct(true);
		}
		model.addAttribute("productForm", productForm);
		return "product";
	}

	// POST: Save product
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("productForm") @Validated ProductForm productForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "product";
		}
		try {
			productDAO.save(productForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "product";
		}
		return "redirect:/productList";
	}

	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
	public String orderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = this.orderDAO.getOrderInfo(orderId);
		}
		if (orderInfo == null) {
			return "redirect:/admin/orderList";
		}
		List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
		orderInfo.setDetails(details);

		model.addAttribute("orderInfo", orderInfo);

		return "order";
	}

	@RequestMapping(value = { "/admin/userList/setActive" }, method = RequestMethod.GET)
	public String disableUser(Model model, @RequestParam(value = "userName") String userName,
			@RequestParam(value = "setValue") String setValue,
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		Account account = accountDAO.findAccount(userName);

		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<AccountInfo> paginationResult //
				= accountDAO.setActiveAccount(account, page, MAX_RESULT, MAX_NAVIGATION_PAGE,Integer.parseInt(setValue));
		model.addAttribute("paginationResult", paginationResult);
		return "userList";
	}

}