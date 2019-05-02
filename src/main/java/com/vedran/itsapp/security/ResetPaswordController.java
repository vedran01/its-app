package com.vedran.itsapp.security;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.repository.ItsUserRepository;
import com.vedran.itsapp.service.ItsMailService;
import com.vedran.itsapp.util.Response;
import com.vedran.itsapp.util.error.exceptions.BadRequestException;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Log
@RestController
@RequestMapping("/password")
public class ResetPaswordController {

  @Value("${its.security.password-recovery-url}")
  private String recoveryBaseUrl;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private ItsUserRepository userRepository;
  @Autowired
  private ItsJwtHelper jwt;
  @Autowired
  private ItsMailService mailService;

  @RequestMapping("/{email}")
  public ResponseEntity<Response> sendResetToken(@PathVariable String email) {
    return userRepository.findByEmail(email).map(this::handleResetToken)
            .orElseThrow(() -> new ResourceNotFoundException(ItsUser.class, "email" , email));

  }
  // TODO SEND EMAIL
  private ResponseEntity<Response> handleResetToken(ItsUser user){
    UUID uuid = UUID.randomUUID();
    user.setPasswordResetToken(uuid.toString());
    userRepository.save(user);
    String token = jwt.generatePasswordResetToken(user, uuid);
    mailService.sendPasswordResetMail(user.getEmail(),recoveryBaseUrl + token );
    log.info(recoveryBaseUrl + token);
    return ResponseEntity.ok(new Response("A password reset message has been sent to your email address."));
  }


  @PostMapping("/reset")
  ResponseEntity<Response> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
    try {
      Claims claims = jwt.getClaimsFromToken(request.getToken());
      String userId = claims.getSubject();
      String passwordResetToken = String.valueOf(claims.get("uuid"));

      ItsUser user = userRepository.findByIdAndPasswordResetToken(userId,passwordResetToken)
              .orElseThrow(() -> new BadRequestException("Token not valid"));

        String password = passwordEncoder.encode(request.getPassword());
        user.setPassword(password);
        user.setPasswordResetToken(null);
        userRepository.save(user);

        return ResponseEntity.ok(new Response("Your new password has been saved please use your new password when you login to."));
    }
    catch (JwtException ex){
          throw new BadRequestException("Password link expired or not valid.");
    }
  }
}


