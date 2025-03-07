package com.gisele.RoadandutilityInsepectionmanagementsystem.controller;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Role;
import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import com.gisele.RoadandutilityInsepectionmanagementsystem.dao.UserDao;
import com.gisele.RoadandutilityInsepectionmanagementsystem.service.ManagerService;
import com.gisele.RoadandutilityInsepectionmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;


@Controller
@RequestMapping("/Manager")
public class ManagerController {

   // @Autowired
    private UserService userService;
   // @Autowired
    private ManagerService managerService;


    private JavaMailSender mailSender;

    private UserDao userDao;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public ManagerController(UserService userService, ManagerService managerService,
                             UserDao userDao, JavaMailSender mailSender) {
        this.userService = userService;
        this.managerService = managerService;
        this.userDao = userDao;
        this.mailSender = mailSender;
    }


    private Logger logger = Logger.getLogger(getClass().getName());

    // add an initbinder ... to convert trim input strings
    // remove leading and trailing whitespace
    // resolve issue for our validation
    @InitBinder
    public void initBinder(WebDataBinder dataBinder)
    {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model theModel) {
        User user = new User();
        theModel.addAttribute("user", user);
        return "Authentication/registration-form";
    }
    @GetMapping("/listUsers")
    public String getAllUsers(Model theModel)
    {
        String keyword = null;
        return getUsersPage(0, 2, keyword, theModel);
    }
    @GetMapping("/users/{pageNumber}")
    public String getUsersPage(
//            @RequestParam(defaultValue = "0") int page,
                               @PathVariable("pageNumber") int page,
                               @RequestParam(defaultValue = "2") int size,
                               @Param("keyword") String keyword,
                               Model model) {

        //List<User> users = userDao.findUsers(page, size);
        List<User> users = userDao.findUsers(page, size,keyword);
        long totalUsers = userDao.countUsers();
        int totalPages = (int) Math.ceil((double) totalUsers / size);
        for(User user:users)
        {
            logger.info(user.toString());
        }
        logger.info("currentPage" + page);
        logger.info("totalPages" + totalPages);

        model.addAttribute("currentPage", page+1);
        model.addAttribute("TotalItems", totalUsers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "Thymeleaf/usersList";
    }
    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, @RequestParam("mobile")String mobile,
                                 HttpSession session, Model theModel) {
        User existing = userService.findByEmailAndTelNumber(email, mobile);

        if(existing!=null){
            List<Role> roles = (List<Role>) existing.getRoles();
            for(Role role:roles){
                logger.info(role.toString());
            }
            session.setAttribute("resetId", existing.getId());
            session.setAttribute("Reset_roles", roles);
            session.setAttribute("existing", existing);
            return "redirect:/showResetPassword";
        }else{
            session.setAttribute("msg", "Invalid email & phone number");
            return "redirect:/showForgotPassword";
        }
    }

    @PostMapping("/changePassword")
    public  String changePassword(
//            @RequestParam("psw") String psw, @RequestParam("resetId") Long id,
                                @Valid @ModelAttribute("existing") User existing,
                                BindingResult theBindingResult,
                                Model model,
                                HttpSession session){

        logger.info(">>>>>>>>>> Stage 1 ");
        if(theBindingResult.hasErrors()){
            logger.info(">>>>>>>>>> You have errors>>>>>>>>>>>>>>>."+theBindingResult.toString());

            return "Authentication/ResetPassword";
        }

        User user = userService.findById(existing.getId());
        if(user!=null || existing!=null)
        {
            logger.info("User found...About to change password");
            existing.setPassword(existing.getPassword()); ///try123

            logger.info(existing.toString());
            try{
                userService.saveUpdate(existing);
                session.setAttribute("msgSuccess", "Password Changed Successfully");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            logger.info("User not found... Change password operation no effect");
        }

        return "redirect:/showForgotPassword";
    }

    @PostMapping("/processForm")
    public String processForm(
            @Valid @ModelAttribute("user") User user,
            BindingResult theBindingResult,
            HttpSession session,
            Model theModel) {
        String userName = user.getUserName();
        logger.info("Processing registration form for: " + userName
        + " With role = " + user.getFormRole());
        if(theBindingResult.hasErrors()){
            return "Authentication/registration-form";
        }
        // check the database if user already exists
        User existing = userService.findByUserName(userName);
        if (existing != null){
            theModel.addAttribute("user",  new User());
            theModel.addAttribute("registrationError", "User name already exists.");
            logger.warning("User name already exists.");
            return "Authentication/registration-form";
        }
        //
        //We passed all the validation checks!
        // let's get down to business!!!
        //

        // encrypt the password
        //String encodedPassword = passwordEncoder.encode(operationsManager.getPassword());

        // prepend the encoding algorithm id
        //encodedPassword = "{bcrypt}" + encodedPassword;

        // give user default role of "inspector"
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList();
        authorities.add(new SimpleGrantedAuthority("ROLE_PROJECT_MANAGER"));

        // if the user selected role other than employee,
        // then add that one too (multiple roles)
        String formRole = user.getFormRole();

        if (!formRole.equals("ROLE_PROJECT_MANAGER")) {
            if (formRole.equals("ROLE_INSPECTOR")) {
                authorities.add(new SimpleGrantedAuthority(formRole));
            }else if(formRole.equals("ROLE_OPERATIONS_MANAGER")){
                authorities.add(new SimpleGrantedAuthority("ROLE_INSPECTOR"));
                authorities.add(new SimpleGrantedAuthority(formRole));
            }
        }
        logger.info("=========roles========");
        for(GrantedAuthority auth:authorities){
            logger.info(auth.toString() + "\n");
        }

        // create user account
        userService.save(user, authorities);
        session.setAttribute("userSuccess", "Successfully created user");
        logger.info("Successfully created user: " + userName);

        return "redirect:/showMyLoginPage";
    }

    @PostMapping("/approve")
    public String approveUser(@RequestParam("userId") Long userId, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(userId);
        if (user != null) {
            // Send approval email without updating user or setting status
            boolean emailSent = sendApprovalEmail(user.getEmail());

            if (emailSent) {
                redirectAttributes.addAttribute("success", true);
            } else {
                redirectAttributes.addAttribute("error", true);
            }
        }

        return "redirect:/Manager/users/0";
    }

    @PostMapping("/reject")
    public String rejectUser(@RequestParam("userId") Long userId, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(userId);
        if (user != null) {
            // Send rejection email without updating user or setting status
            boolean emailSent = sendRejectionEmail(user.getEmail());

            if (emailSent) {
                redirectAttributes.addAttribute("success", true);
            } else {
                redirectAttributes.addAttribute("error", true);
            }
        }

        return "redirect:/Manager/users/0";
    }

    private boolean sendApprovalEmail(String recipientEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Account Approved");
            message.setText("Your account has been approved. You can now log in to the system.");
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception as needed
            return false;
        }
    }

    private boolean sendRejectionEmail(String recipientEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Account Rejected");
            message.setText("Your account has been rejected. If you have any questions, please contact support.");
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception as needed
            return false;
        }
    }

}
