package br.com.appcontrole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AplicaçãoController {
	@GetMapping("/index") 
    public String index() {
        return "index";
    }
	
	@GetMapping("/login") 
	public String login() {
        return "login";
    }
}
