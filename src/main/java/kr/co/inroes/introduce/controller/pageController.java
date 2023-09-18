package kr.co.inroes.introduce.controller;

import java.util.Properties;
import javax.mail.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

@Controller
public class pageController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public String email(HttpServletRequest httpServletRequest) throws MessagingException {
        String title = httpServletRequest.getParameter("name");
        String email = httpServletRequest.getParameter("email");
        String content = httpServletRequest.getParameter("message");

        String recipient = "yakulut@inroes.co.kr";

        // 1. 발신자의 메일 계정과 비밀번호 설정
        final String user = "yakulut@inroes.co.kr";
        final String password = "aodbahsjwoicrywv";

        // 2. Property에 SMTP 서버 정보 설정
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // 3. SMTP 서버정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        // 4. Message 클래스의 객체를 사용하여 수신자와 내용, 제목의 메시지를 작성한다.
        // 5. Transport 클래스를 사용하여 작성한 메세지를 전달한다.

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(user));

        // 수신자 메일 주소
        try {
            InternetAddress recipientAddress = new InternetAddress(recipient);
            // 유효한 이메일 주소 형식인지 확인
            if (recipientAddress.getAddress() == null || recipientAddress.getAddress().isEmpty()) {
                // 이메일 주소가 비어있거나 null일 경우 예외 처리
                throw new AddressException("Invalid email address: " + recipient);
            }

            message.addRecipient(Message.RecipientType.TO, recipientAddress);
        } catch (AddressException e) {
            e.printStackTrace();
            // 예외 처리: 이메일 주소 형식이 잘못된 경우 등
        } catch (MessagingException e) {
            e.printStackTrace();
            // 예외 처리: 메일 전송 오류 등
        }

        message.setSubject(title);
        message.setText("연락요청한 이메일 ["+email+"]\n" + content);
        Transport.send(message);

        return "redirect:/";
    }
}
