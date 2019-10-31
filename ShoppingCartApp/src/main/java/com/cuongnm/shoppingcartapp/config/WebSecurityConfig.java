package com.cuongnm.shoppingcartapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cuongnm.shoppingcartapp.service.UserDetailsInfo;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
   @Autowired
   UserDetailsInfo  userDetailsInfo;
 
   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
      return bCryptPasswordEncoder;
   }
 
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
 
      // Setting Service to find User in the database.
      // And Setting PassswordEncoder
      auth.userDetailsService(userDetailsInfo).passwordEncoder(passwordEncoder());
 
   }
 
   @Override
   protected void configure(HttpSecurity http) throws Exception {
 
      http.csrf().disable();
 
      
      http.authorizeRequests().antMatchers("/admin/orderList","/admin/order","/admin/accountInfo","/admin/product").access("hasRole('ROLE_MANAGER')");
      
      
      http.authorizeRequests().antMatchers("/orderList","/order","/accountInfo").access("hasRole('ROLE_EMPLOYEE')");
      
      
      // When user login, role XX.
      // But access to the page requires the YY role,
      // An AccessDeniedException will be thrown.
      http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
 
      // Configuration for Login Form.
      http.authorizeRequests().and().formLogin()
            .loginProcessingUrl("/j_spring_security_check") // Submit URL
            .loginPage("/login")//
            .defaultSuccessUrl("/productList")//
            .failureUrl("/login?error=true")//
            .usernameParameter("userName")//
            .passwordParameter("password")
 
            // Configuration for the Logout page.
            // (After logout, go to home page)
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
   
   }
}
