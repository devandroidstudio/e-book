package com.eCommerce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ECommerceApplicationTests {

	@Test
	public void contextLoads() {
		double n = 1240.35;

		NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
		String val = nf.format(n);
		System.out.println(val);
	}

}
