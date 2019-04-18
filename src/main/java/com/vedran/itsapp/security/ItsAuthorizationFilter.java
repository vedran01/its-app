package com.vedran.itsapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ItsAuthorizationFilter extends OncePerRequestFilter {

  private final ItsJwtHelper jwtHelper;
  private final ItsUserDetailsService service;

  ItsAuthorizationFilter(ItsJwtHelper jwtHelper, ItsUserDetailsService service){
    this.jwtHelper = jwtHelper;
    this.service = service;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String HEADER = "Authorization";
    String token = request.getHeader(HEADER);

    try {

      Claims claims = jwtHelper.getClaimsFromToken(token);
      UserDetails userDetails =  service.loadUserByUsername(claims.getSubject());
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    catch (UsernameNotFoundException | IllegalArgumentException |JwtException e){
      log.debug(e.getMessage());
    }

    chain.doFilter(request,response);
  }
}
