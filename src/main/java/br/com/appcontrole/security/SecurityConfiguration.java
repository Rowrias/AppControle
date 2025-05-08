package br.com.appcontrole.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.funcionario.FuncionarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
	        .formLogin(formLogin -> formLogin
        		// Especifica a página de login personalizada
        		.loginPage("/login")
        		// Permite acesso à página de login sem autenticação
        		.permitAll()
    		)
	        .logout(logout -> logout
        		// URL para logout
        		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        		// Redireciona para a página de login após o logout
        		.logoutSuccessUrl("/login")
    		)
	        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        		.requestMatchers(HttpMethod.GET, "/funcionarios/**", "/clientes/**", "/produtos/**", "/saidas/**").hasRole("ADMIN")
        		.requestMatchers(HttpMethod.POST, "/funcionarios/**", "/clientes/**", "/produtos/**", "/saidas/**").hasRole("ADMIN")
        		// Todas as outras requisições devem ser autenticadas
        		.anyRequest().authenticated()
    		)
	        .exceptionHandling(exception -> exception
                .accessDeniedPage("/sem-acesso")
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
	
	@Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public UserDetailsService userDetailsService(FuncionarioRepository funcionarioRepository) {
	    return username -> {
	        Funcionario funcionario = funcionarioRepository.findByUsername(username);
	        if (funcionario != null) {
	            return User.withUsername(funcionario.getUsername())
	                .password(funcionario.getPassword())
	                .roles(funcionario.getRole().name())
	                .build();
	        } else {
	            throw new UsernameNotFoundException("Usúario não encontrado");
	        }
	    };
	}
}
