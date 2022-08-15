package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	@Value("${forum.jwt.expiration}")
	private String expiration; //injetando configuração do application.properties
	@Value("${forum.jwt.secret}")
	private String secret;
	//gera o token
	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario)authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API do fórum da Alura") //quem fez a aplicação que fez a geração do token
				.setSubject(logado.getId().toString()) //quem é o dono to token
				.setIssuedAt(hoje) //qual a data de geração do token
				.setExpiration(dataExpiracao) //data de expiração pra não durar pra sempre
				.signWith(SignatureAlgorithm.HS256, secret) //criptografando o token, dizendo quem é o algoritmo e a senha da aplicação
				.compact(); //fala pra compactar e gerar uma string
	}
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}
	

}
