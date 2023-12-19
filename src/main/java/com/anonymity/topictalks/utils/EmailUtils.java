package com.anonymity.topictalks.utils;

import com.anonymity.topictalks.models.dtos.MailDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.utils
 * - Created At: 25-10-2023 17:10:42
 * @since 1.0 - version of class
 */

@Component
public class EmailUtils {

    @Autowired
    private  JavaMailSender mailSender;

    @Autowired
    private  Configuration templateConfiguration;

    @Value("${app.velocity.templates.location}")
    private String basePackagePath;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    public void sendOtpEmail(String email, String otp)
            throws IOException, TemplateException, MessagingException {
        MailDTO mail = new MailDTO();
        mail.setSubject("Email Verification [Team Support Topictalks]");
        mail.setTo(email);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", email);
        String[] otpDigits = otp.split("");
        mail.getModel().put("otpDigit1", otpDigits[0]);
        mail.getModel().put("otpDigit2", otpDigits[1]);
        mail.getModel().put("otpDigit3", otpDigits[2]);
        mail.getModel().put("otpDigit4", otpDigits[3]);
        mail.getModel().put("otpDigit5", otpDigits[4]);
        mail.getModel().put("otpDigit6", otpDigits[5]);
        mail.getModel().put("userEmailTokenVerificationLink", "https://www.topictalks.online/verify-account?email="+email+"&otp="+otp);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("email-verification.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    public void sendActiveAccount(String email) throws IOException, TemplateException, MessagingException {
        MailDTO mail = new MailDTO();
        mail.setSubject("Account Status Change [Team Support Topictalks]");
        mail.setTo(email);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", email);
        mail.getModel().put("action", "Account Activation");
        mail.getModel().put("actionStatus", "Completed");

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    public void sendSetPassword(String email) throws IOException, TemplateException, MessagingException {
        MailDTO mail = new MailDTO();
        mail.setSubject("Account Status Change [Team Support Topictalks]");
        mail.setTo(email);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", email);
        mail.getModel().put("action", "Password Changing");
        mail.getModel().put("actionStatus", "Completed");

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    public void send(MailDTO mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo());
        helper.setText(mail.getContent(), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        mailSender.send(message);
    }

    public void sendForgotEmail(String email, String token) throws IOException, TemplateException, MessagingException {
        MailDTO mail = new MailDTO();
        mail.setSubject("Email Verification [Team Support Topictalks]");
        mail.setTo(email);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", email);
        mail.getModel().put("userForgotPasswordLink", "https://www.topictalks.online/forgot-password?token="+token);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("forgot-password-mail.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

//    public void sendEmailVerification(String emailVerificationUrl, String to)
//            throws IOException, TemplateException, MessagingException {
//        MailDTO mail = new MailDTO();
//        mail.setSubject("Email Verification [Team CEP]");
//        mail.setTo(to);
//        mail.setFrom(mailFrom);
//        mail.getModel().put("userName", to);
//        mail.getModel().put("userEmailTokenVerificationLink", emailVerificationUrl);
//
//        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
//        Template template = templateConfiguration.getTemplate("email-verification.ftl");
//        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
//        mail.setContent(mailContent);
//        send(mail);
//    }
//
//    /**
//     * Setting the mail parameters.Send the reset link to the respective user's mail
//     */
//    public void sendResetLink(String resetPasswordLink, String to)
//            throws IOException, TemplateException, MessagingException {
//        Long expirationInMinutes = TimeUnit.MILLISECONDS.toMinutes(expiration);
//        String expirationInMinutesString = expirationInMinutes.toString();
//        MailDTO mail = new MailDTO();
//        mail.setSubject("Password Reset Link [Team CEP]");
//        mail.setTo(to);
//        mail.setFrom(mailFrom);
//        mail.getModel().put("userName", to);
//        mail.getModel().put("userResetPasswordLink", resetPasswordLink);
//        mail.getModel().put("expirationTime", expirationInMinutesString);
//
//        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
//        Template template = templateConfiguration.getTemplate("reset-link.ftl");
//        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
//        mail.setContent(mailContent);
//        send(mail);
//    }
//
//    /**
//     * Send an email to the user indicating an account change event with the correct
//     * status
//     */
//    public void sendAccountChangeEmail(String action, String actionStatus, String to)
//            throws IOException, TemplateException, MessagingException {
//        MailDTO mail = new MailDTO();
//        mail.setSubject("Account Status Change [Team CEP]");
//        mail.setTo(to);
//        mail.setFrom(mailFrom);
//        mail.getModel().put("userName", to);
//        mail.getModel().put("action", action);
//        mail.getModel().put("actionStatus", actionStatus);
//
//        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
//        Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
//        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
//        mail.setContent(mailContent);
//        send(mail);
//    }

}
