package br.com.appcontrole.domain;

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
	
	@GetMapping("/configuracao")
    public String configuracao() {
        return "configuracao";
    }
	
	@GetMapping("/sem-acesso")
    public String acessoNegado() {
        return "sem-acesso";
    }

}
