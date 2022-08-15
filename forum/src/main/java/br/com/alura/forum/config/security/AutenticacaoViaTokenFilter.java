package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UserRepository;

//criando um filtro
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{
	
	private TokenService tokenService;
	private UserRepository userRepository;
	
	public AutenticacaoViaTokenFilter(TokenService tokenService,UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	 @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);
        boolean valido = tokenService.isTokenValido(token);
        if(valido) {
        	autenticarToken(token);
        }
        filterChain.doFilter(request,response);

    }
	 //diz se o cliente esta autenticado
	private void autenticarToken(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = userRepository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
			
	}
	
	//recupera o token que o filter pegou
	private String recuperarToken(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");

	        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")){
	            return null;
	        }
	        return token.substring(7, token.length());//como o token devolve "bearer token", pegamos só a parte que esta o token

	    }


}
