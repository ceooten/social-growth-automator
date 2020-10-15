package com.jamesmhare.socialgrowthautomator.controller;

import com.jamesmhare.socialgrowthautomator.model.user.Role;
import com.jamesmhare.socialgrowthautomator.model.user.User;
import com.jamesmhare.socialgrowthautomator.service.EmailService;
import com.jamesmhare.socialgrowthautomator.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping(value="/register")
    public String showRegistrationForm(final User user){
        return "user-registration";
    }

    @PostMapping(value = "/register")
    public String processRegistrationForm(@Valid final User user, final BindingResult result, final Model model, HttpServletRequest request) {

        // Check that there isn't already an account registered with the given email address.
        if (userService.findByEmail(user.getEmail()) != null) {
            return "user-registration";
        }

        if (result.hasErrors()) {
            return "user-registration";
        } else {
            // Add new user and send confirmation email

            // Disable user until they click on confirmation link in email
            user.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());
            user.setSchemaName("sga_" + user.getUsername());
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(2, "USER"));
            user.setRoles(roles);
            userService.saveUser(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Social Growth Automator: Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + "/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("socialgrowthautomator@gmail.com");

            emailService.sendEmail(registrationEmail);

            // modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
            return "user-registration-email-sent";
        }
    }

    // Process confirmation link
    @GetMapping(value="/confirm")
    public String showConfirmationPage(final Model model, @RequestParam("token") String token) {
        if (userService.findByConfirmationToken(token) == null) { // No token found in DB
            // model.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            // model.addObject("confirmationToken", user.getConfirmationToken());
        }
        return "user-registration-token-accepted";
    }

    // Process confirmation link
    @RequestMapping(value="/confirm", method = RequestMethod.POST)
    public String processConfirmationForm(final Model model, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redir) {
        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(requestParams.get("password").toString());
        if (strength.getScore() < 3) {
            bindingResult.reject("password");
            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
            // modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            // return modelAndView;
        }

        // Find the user associated with the reset token
        User user = userService.findByConfirmationToken(requestParams.get("token").toString());

        // Set new password
        user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password").toString()));

        // Set user to enabled
        user.setEnabled(true);

        // Save user
        userService.saveUser(user);

        return "user-registration-success";
    }

}
