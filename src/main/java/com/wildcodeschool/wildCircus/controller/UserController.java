package com.wildcodeschool.wildCircus.controller;

import com.wildcodeschool.wildCircus.entity.Cours;
import com.wildcodeschool.wildCircus.entity.Inscription;
import com.wildcodeschool.wildCircus.entity.Role;
import com.wildcodeschool.wildCircus.entity.User;
import com.wildcodeschool.wildCircus.repository.CoursRepository;
import com.wildcodeschool.wildCircus.repository.InscriptionRepository;
import com.wildcodeschool.wildCircus.repository.RoleRepository;
import com.wildcodeschool.wildCircus.repository.UserRepository;
import com.wildcodeschool.wildCircus.service.EmailService;
import com.wildcodeschool.wildCircus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EmailService emailService;


    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/register")
    public String getRegister() {

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(HttpServletRequest request,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.getRoles().add(optionalRole.get());
            userRepository.save(user);
            userService.autoLogin(request, username, password);
            emailService.sendNewUserEmail(user.getEmail(), username);
            return "redirect:/";
        }

        return "redirect:/register";
    }

    @GetMapping("/login")
    public String getLogIn(Model out) {

        User logged = userService.getLoggedUsername();
        out.addAttribute("cours", coursRepository.findAll());

        return "login";
    }

    @GetMapping("/index")
    public String getUserManagement(Model out) {

        User logged = userService.getLoggedUsername();
        out.addAttribute("cours", coursRepository.findAll());

        return "index";
    }

    @GetMapping("/inscription")
    public String getInscription(Model out) {

        Inscription inscription = new Inscription();
        out.addAttribute("coursList", coursRepository.findAll());
        out.addAttribute("inscription", inscription);

        return "inscription";
    }

    @PostMapping("/inscription")
    public String postInscription(@ModelAttribute Inscription inscription) {

         User user = userService.getLoggedUsername();
         inscription.setUser(user);
         inscriptionRepository.save(inscription);
         emailService.sendNewInscription(user.getEmail(), user.getUsername(), inscription);
         return "redirect:/index";
     }


    @GetMapping("/profil")
    public String getProfileUser(Model out) {

        User user = userService.getLoggedUsername();
        out.addAttribute("user", user);
        out.addAttribute("inscription", inscriptionRepository.findAllByUser(user));
        return "profil";
    }

    @PostMapping("/profil")
    public String modificationProfile(@ModelAttribute User user) {

        User logged = userService.getLoggedUsername();
        logged.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(logged);
        /*user.setUsername(user.getUsername());
        userRepository.save(user);*/

        return "profil";
    }


}
