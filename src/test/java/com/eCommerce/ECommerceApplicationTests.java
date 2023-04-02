package com.eCommerce;

import com.eCommerce.domain.Order;
import com.eCommerce.domain.security.Role;
import com.eCommerce.domain.security.UserRole;
import com.eCommerce.repository.RoleRepository;
import com.eCommerce.repository.UserRolesRepository;
import com.eCommerce.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ECommerceApplicationTests {
	@MockBean
	private OrderService orderService;

	@Test
	public void contextLoads() {

		int sum = 0;
		List<Integer> list = new ArrayList<>();

		for (int i = 1; i < 13; i++) {
			for (Order o : orderService.findAllOrder()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = simpleDateFormat.format(o.getOrderDate().getMonth());
				if (i == Integer.parseInt(date)){
					sum += o.getOrderTotal().intValue();
				}
			}
			list.add(sum);
			sum = 0;

		}

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i) + "");
		}
	}

}
