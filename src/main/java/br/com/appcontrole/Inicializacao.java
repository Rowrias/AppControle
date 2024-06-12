package br.com.appcontrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.appcontrole.model.Funcionario;
import br.com.appcontrole.repository.FuncionarioRepository;

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
            funcionario.setUsername("adm");
            funcionario.setPassword(passwordEncoder.encode("123"));
            funcionario.setRole("ADM");
            funcionarioRepository.save(funcionario);
        }
    }
}
