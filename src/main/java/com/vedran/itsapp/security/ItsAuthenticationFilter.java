package com.vedran.itsapp.security;

import com.vedran.itsapp.model.ItsUser;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ItsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ItsJwtHelper jwt;
  private AuthenticationManager authenticationManager;

  ItsAuthenticationFilter(AuthenticationManager authenticationManager, ItsJwtHelper jwt) {
    this.authenticationManager = authenticationManager;
    this.jwt = jwt;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String loginMail = obtainUsername(request);
    String password = obtainPassword(request);
    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginMail,password));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String token = jwt.generateAuthorizationToken((ItsUser) authResult.getPrincipal());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(String.format("{\"auth-token\": \"%s\"}", token));
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    response.sendError(401, "Invalid mail or password " + failed.getMessage());
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getParameter("email");
  }

}

