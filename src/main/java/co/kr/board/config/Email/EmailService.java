package co.kr.board.config.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    //인증번호 생성
    private final String ePw = createKey();

    //사용자의 아이디
    @Value("${spring.mail.username}")
    private String id;

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    /**
     * 비밀번호 재설정에 필요한 이메일 문자
     * @author 양경빈
     * @param userEmail 회원의 이메일
     * MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
     * bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
     * @exception MessagingException 메시지를 보내는데 문제가 있는 경우 exception
     * @exception UnsupportedEncodingException 인코딩에 문제가 있는경우 exception
     **/
    public MimeMessage createPasswordMessage(String userEmail)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ userEmail);
        log.info("인증 번호 : " + ePw);

        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, userEmail); // to 보내는 대상
        message.setSubject("비밀번호 재설정 인증입니다."); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 입력 화면에 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    /**
     * 비밀번호 재설정 이메일 발송
     * @author 양경빈
     * @param userEmail 임시번호를 발급받는 회원의 이메일
     * @exception MessagingException 메시지 에러
     * @exception UnsupportedEncodingException 인코딩이 안되는 경우의 Exception
     **/
    @Async
    public String sendTemporaryPasswordMessage(String userEmail)throws MessagingException,UnsupportedEncodingException {
        MimeMessage message = createPasswordMessage(userEmail);

        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }

    /**
     * 비밀글 전환시 보낼 이메일 메시지
     * 관리자 페이지에서 비밀글로 전환을 했을시 게시글 작성자의 메일로 임시번호를 보내는 기능
     **/
    public MimeMessage createSecretPasswordMessage(String userEmail,String secretPassword)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ userEmail);
        log.info("인증 번호 : " + secretPassword);

        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, userEmail); // to 보내는 대상
        message.setSubject("비밀글 비밀번호 메일입니다."); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">임시비밀번호</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">게시글이 비밀글로 변경되었으므로 임시번호를 발급합니다.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += secretPassword;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    /**
     * 비밀글 전환시 보낼 이메일 전송
     * 관리자 페이지에서 비밀글로 전환을 했을시 게시글 작성자의 메일로 임시번호를 보내는 기능
     **/
    @Async
    public String sendTemporarySecretPasswordMessage(String userEmail,String secretPassword)throws Exception{
        MimeMessage message = createSecretPasswordMessage(userEmail, secretPassword);

        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return secretPassword;
    }

}
