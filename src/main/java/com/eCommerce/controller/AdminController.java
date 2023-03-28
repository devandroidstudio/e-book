package com.eCommerce.controller;

import com.eCommerce.domain.Order;
import com.eCommerce.service.BookService;
import com.eCommerce.service.OrderService;
import com.eCommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;
    @GetMapping("/admins")
    public String indexAdmin(Model model){
        int totalOrder = orderService.findAllOrder().size();
        int totalUser = userService.findAllUser().size();
        int totalSale = 0;
        for (Order order : orderService.findAllOrder()) {
            totalSale += order.getOrderTotal().intValue();
        }

        model.addAttribute("totalOrder", totalOrder);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("listOrder", orderService.findAllOrder());
        model.addAttribute("totalSale", totalSale);
        return "admins/dashboard";
    }
    @GetMapping("/admins/book")
    public String bookPage(Model model){
        model.addAttribute("listBook", bookService.findAll());
        return "admins/book";
    }
    @GetMapping("/admins/users")
    public String userPage(){
        return "admins/users";
    }
    @GetMapping("/admins/orders")
    public String orderPage(){
        return "admins/orders";
    }
}
