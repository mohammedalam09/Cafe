package com.cafe.example.cafeExample.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private String secretkey = "cafeManagement";

	public String extractUsername(String token) {
		return extractClamis(token, Claims::getSubject);

	}

	public Date extractExpirationTime(String token) {
		return extractClamis(token, Claims::getExpiration);
	}

	public <T> T extractClamis(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);

	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();

	}

	private Boolean isTokenExpired(String token) {
		return extractExpirationTime(token).before(new Date());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
				.signWith(SignatureAlgorithm.HS256, secretkey)
				.compact();
	}

	public String generateToken(String username, String role) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("role", role);
		return createToken(claims, username);
	}

	public boolean validateToken(String token, UserDetails userdetails) {
		String username = extractUsername(token);
		return (username.equalsIgnoreCase(userdetails.getUsername()) && !isTokenExpired(token));
	}

}
