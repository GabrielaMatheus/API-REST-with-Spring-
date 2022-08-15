package br.com.alura.forum.config.security;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import br.com.alura.forum.repository.UserRepository;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class SecurityConfigurations {
	
	@Autowired
	private TokenService tokenService;
	@Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private UserRepository userRepository;
    
	//configuracoes de autenticacao
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    } //injetando o AuthenticationManager no controller

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	//configurações de autorização
 	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .antMatchers(HttpMethod.GET,"/topicos").permitAll()
            .antMatchers(HttpMethod.GET,"/topicos/*").permitAll()
            .antMatchers(HttpMethod.POST,"/auth").permitAll()
            .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
            .antMatchers(HttpMethod.DELETE,"/topicos/*").hasRole("MODERADOR") //permite deletar só quem tem o perfil de MODERADOR
            .anyRequest().authenticated() //authenticated indica que as URLs não configuradas tem acesso restrito
            .and().csrf().disable() //csrf é uma habilitação pra prever um ataque web. Mas como ja vamos usar token, ja nos precavemos desse ataque, então podemos desabilitar
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService,userRepository), UsernamePasswordAuthenticationFilter.class); //habilitando nosso filter
        
        return http.build();
    }
 	
 	
 	
 	
 	
 	
	  
 	
	  

}
