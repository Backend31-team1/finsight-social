package com.project.auth.client.mailgun;

import com.project.auth.client.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "${mailgun.api.base-url}" )
@Qualifier("mailgun")
public interface MailgunClient {

  @PostMapping("/{domain}/messages")
  ResponseEntity<String> sendEmail(
      @PathVariable String domain,
      @SpringQueryMap SendMailForm form);
}
