package com.eCommerce.controller;

import com.eCommerce.domain.Book;
import com.eCommerce.domain.Category;
import com.eCommerce.domain.Order;
import com.eCommerce.domain.User;
import com.eCommerce.domain.security.Role;
import com.eCommerce.domain.security.UserRole;
import com.eCommerce.repository.CategoryRepository;
import com.eCommerce.service.BookService;
import com.eCommerce.service.FilesStorageService;
import com.eCommerce.service.OrderService;
import com.eCommerce.service.UserService;
import com.eCommerce.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    FilesStorageService storageService;




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
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        model.addAttribute("totalSale", nf.format(totalSale).concat("$"));
        model.addAttribute("listUser", userService.findAllUser());
        return "admins/dashboard";
    }
    @GetMapping("/admins/book")
    public String bookPage(Model model){
        model.addAttribute("listBook", bookService.findAll());
        return "admins/book";
    }

    @GetMapping("/admins/book/editBook")
    public String createBookPage(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "/admins/editBook";
    }

    @GetMapping("/admins/book/editBook/{id}")
    public String editBookPage(@PathVariable("id") Long id, Model model){
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryRepository.findAll());
        return "/admins/editBook";
    }

    @PostMapping("/admins/book/save")
    public String saveBookPage(Book bookCurrent, Principal principal){
        if(!bookCurrent.getBookImage().getOriginalFilename().equals(bookCurrent.getImageProduct()) && !bookCurrent.getBookImage().getOriginalFilename().isEmpty() && bookCurrent.getBookImage().getOriginalFilename() != null){
            bookCurrent.setImageProduct(bookCurrent.getBookImage().getOriginalFilename());
            storageService.save(bookCurrent.getBookImage());
        }
        bookCurrent.setPublisher(principal.getName());
        bookCurrent.setOurPrice(bookCurrent.getListPrice() * (1 - ((double) bookCurrent.getDiscount() / 100)));
        bookService.save(bookCurrent);

        return "redirect:/admins/book";
    }

    @GetMapping("/admins/book/delete/{id}")
    public String deleteBookPage(@PathVariable("id") Long id, RedirectAttributes ra, Model model){
        try {
            bookService.delete(id);
            ra.addFlashAttribute("message", "Delete the image successfully: " + bookService.findById(id).getTitle());
        }catch (Exception e){
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admins/book";
    }



    //Manager User
    @GetMapping("/admins/users")
    public String userPage(Model model){
        model.addAttribute("users",userService.findAllUser());
        model.addAttribute("user", new User());
        return "admins/users";
    }
    @PostMapping("/admins/users/create")
    public String newUserPage(User userCurrent, RedirectAttributes ra, Model model) throws Exception{

        if (userService.findByUsername(userCurrent.getUsername()) != null) {
            model.addAttribute("usernameExists", true);

            return "redirect:/admins/users";
        }

        if (userService.findByEmail(userCurrent.getEmail()) != null) {
            model.addAttribute("emailExists", true);

            return "redirect:/admins/users";
        }

        User user = new User();
        user.setId(userCurrent.getId());
        user.setUsername(userCurrent.getUsername());
        user.setEmail(userCurrent.getEmail());
        user.setFirstName(userCurrent.getFirstName());
        user.setLastName(userCurrent.getLastName());
        user.setEnabled(true);

        user.setPhone(userCurrent.getPhone());

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(userCurrent.getPassword());
        user.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        return "redirect:/admins/users";
    }


    @PostMapping("/admins/users/save")
    public String showNewForm(Model model, User userCurrent){
        User user = new User();
        user.setId(userCurrent.getId());
        user.setUsername(userCurrent.getUsername());
        user.setEmail(userCurrent.getEmail());
        user.setFirstName(userCurrent.getFirstName());
        user.setLastName(userCurrent.getLastName());
        user.setEnabled(true);

        user.setPhone(userCurrent.getPhone());

       if(!userCurrent.getPassword().isEmpty() || userCurrent.getPassword() != null){
           String encryptedPassword = SecurityUtility.passwordEncoder().encode(userCurrent.getPassword());
           user.setPassword(encryptedPassword);
       }
        user.setPassword(user.getPassword());

        userService.save(user);
        return "redirect:/admins/users";
    }
    @GetMapping("/admins/users/edit/{id}")
    public String showEditFormUser(@PathVariable("id") Long id, Model model){
        try {
            User user = userService.findById(id);
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit User (ID: "+ id+")");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "admins/editUser";
    }
    @GetMapping("/admins/users/delete/{id}")
    public String deleteUserPage(@PathVariable("id") Long id, RedirectAttributes ra){
        try {
            userService.delete(id);
            ra.addFlashAttribute("message","Delete user ID:"+id+" successfully");
        }catch (Exception e){
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admins/users";
    }


    //Manager Order
    @GetMapping("/admins/orders")
    public String orderPage(Model model){


        model.addAttribute("listOrder", orderService.findAllOrder());
        return "admins/orders";
    }
}
