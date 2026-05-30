package com.bibliotheque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String accueil() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/catalogue")
    public String catalogue() {
        return "catalogue";
    }

    @GetMapping("/emprunts")
    public String emprunts() {
        return "emprunts";
    }

    @GetMapping("/reservations")
    public String reservations() {
        return "reservations";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin-register")
    public String adminRegister() {
        return "admin-register";
    }

    @GetMapping("/biblio-register")
    public String biblioRegister() {
        return "biblio-register";
    }

    @GetMapping("/biblio")
    public String biblio() {
        return "biblio";
    }

    @GetMapping("/apropos")
    public String apropos() {
        return "apropos";
    }
}