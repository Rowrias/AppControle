package br.com.appcontrole.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.funcionario.FuncionarioRepository;
import br.com.appcontrole.domain.funcionario.Role;

@Component
public class Inicializacao implements CommandLineRunner {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (funcionarioRepository.count() == 0) {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome("Administrador");
            funcionario.setUsername("admin");
            funcionario.setPassword(passwordEncoder.encode("1234"));
            funcionario.setRole(Role.Administrador);
            funcionarioRepository.save(funcionario);
        }
    }
}
