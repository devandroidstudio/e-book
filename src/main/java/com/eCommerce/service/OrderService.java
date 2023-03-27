package com.eCommerce.service;

import com.eCommerce.domain.BillingAddress;
import com.eCommerce.domain.Order;
import com.eCommerce.domain.Payment;
import com.eCommerce.domain.ShippingAddress;
import com.eCommerce.domain.ShoppingCart;
import com.eCommerce.domain.User;

import java.util.List;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	Order findById(Long id);
	List<Order> findAllOrder();
	void removeOrder(Order order);
}
