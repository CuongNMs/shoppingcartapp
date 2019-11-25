package com.cuongnm.shoppingcartapp.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.cuongnm.shoppingcartapp.dao.AccountDAO;

import com.cuongnm.shoppingcartapp.entity.Account;

import com.cuongnm.shoppingcartapp.form.AccountForm;

@Component
public class AccountFormValidator implements Validator {

	@Autowired
	private AccountDAO accountDAO;

	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == AccountForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountForm accountForm = (AccountForm) target;

		// Check the fields of ProductForm.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.accountForm.userName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.accountForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "NotEmpty.accountForm.fullName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.accountForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.accountForm.address");
		if (!emailValidator.isValid(accountForm.getEmail())) {
			errors.rejectValue("email", "Pattern.accountForm.email");
		}
		String name = accountForm.getUserName();
		if (name != null && name.length() > 0) {
			if (name.matches("\\s+")) {
				errors.rejectValue("userName", "Pattern.accountForm.userName");
			} else {
				Account account = accountDAO.findAccount(name);
				if (account != null) {
					errors.rejectValue("userName", "Duplicate.accountForm.userName");
				}
			}
		}
	}
}
