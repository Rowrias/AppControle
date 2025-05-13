package br.com.appcontrole.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AplicaçãoController {
	@GetMapping("/index") 
    public String index() {
        return "index";
    }
	
	@GetMapping("/login2") 
	public String login() {
        return "login2";
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
