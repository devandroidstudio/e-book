package com.eCommerce;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.util.Locale;

import com.sendgrid.*;
import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ECommerceApplicationTests {

	@Test
	public void contextLoads() {
		Email from = new Email("vominhienvai@gmail.com");
		String subject = "Sending with SendGrid is Fun";
		Email to = new Email("vominhhienvai@gmail.com");
		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(System.getenv("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"));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {

		}
	}

}
