
package com.eCommerce.service.impl;
import java.io.IOException;

import com.eCommerce.domain.EmailRequest;
import com.eCommerce.domain.Order;
import com.eCommerce.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class Emailservice {

	@Autowired
	SendGrid sendGrid;

	@Autowired
	TemplateEngine templateEngine;

	public void sendemail(EmailRequest emailrequest) {

		Mail mail = new Mail(new Email("vominhhienvai@gmail.com"), emailrequest.getSubject(),
				new Email(emailrequest.getTo()), new Content("text/plain", emailrequest.getBody()));
		mail.setReplyTo(new Email("vominhhienvai@gmail.com"));
		Request request = new Request();

		Response response = null;

		try {

			request.setMethod(Method.POST);

			request.setEndpoint("mail/send");

			request.setBody(mail.build());

			this.sendGrid.api(request);

		} catch (IOException ex) {

			System.out.println(ex.getMessage());

		}



	}

	public void sendEmailOrder(Order order, User user) {

		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);

		Mail mail = new Mail(new Email("vominhhienvai@gmail.com"), "Order Product",
				new Email(user.getEmail()), new Content("text/plain", text));
		mail.setReplyTo(new Email("vominhhienvai@gmail.com"));
		Request request = new Request();



		try {

			request.setMethod(Method.POST);

			request.setEndpoint("mail/send");

			request.setBody(mail.build());

			this.sendGrid.api(request);

		} catch (IOException ex) {

			System.out.println(ex.getMessage());

		}


	}
}
