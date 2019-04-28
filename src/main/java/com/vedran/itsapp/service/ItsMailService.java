package com.vedran.itsapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class ItsMailService {

  private final TemplateEngine templateEngine;
  private final JavaMailSender javaMailSender;

  public ItsMailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
    this.templateEngine = templateEngine;
    this.javaMailSender = javaMailSender;
  }
  // TODO MANAGE EXCEPTION MAYBE ON OTHER PLACE
  public void sendPasswordResetMail(String to, String url){
    MimeMessagePreparator mimeMessage = message -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(message);
      messageHelper.setSubject("It shop");
      messageHelper.setFrom("it.shop@mail.com");
      messageHelper.setTo(to);
      String content = prepareResetPasswordMessage(url);
      messageHelper.setText(content, true);
    };
    try {
      javaMailSender.send(mimeMessage);
    }
    catch (MailSendException e){
      log.debug(e.getMessage());
    }

  }

  private String prepareResetPasswordMessage(String urlToken){
    Context context = new Context();
    context.setVariable("link", urlToken);
    return templateEngine.process("mail", context);

  }
}
