package serbeii.staj.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.listener.KafkaBackoffException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import serbeii.staj.dto.ErrorDTO;

import java.util.Date;
import java.util.Locale;

@RestControllerAdvice
public class ControllerExceptionHandler {
    // TODO: mesajları geçir
    @Autowired
    MessageSource messageSource;

    @ExceptionHandler({PasswordMatchError.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO passwordExceptionHandler(PasswordMatchError e, WebRequest request) {
        return new ErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Kullanıcı adı veya şifre hatalı.",
                request.getDescription(false)
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO passwordExceptionAuthHandler(BadCredentialsException e) {
        return new ErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Kullanıcı adı veya şifre hatalı.",
                e.getMessage()
        );
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO usernameTakenHandler(UsernameTakenException e, WebRequest request) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Kullanıcı adı kullanımda.",
                request.getDescription(false)
        );
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO emailTakenHandler(EmailTakenException e, WebRequest request) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Eposta adresi kullanımda.",
                request.getDescription(false)
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO userNotFoundHandler(UserNotFoundException e, WebRequest request) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                //"Kullanıcı bulunamadı.",
                messageSource.getMessage("error.usernotfound", null, Locale.getDefault()),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO userNotFoundAuthHandler(InternalAuthenticationServiceException e) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                messageSource.getMessage("error.usernotfound", null, Locale.getDefault()),
                e.getMessage()
        );
    }

    @ExceptionHandler({KafkaException.class, KafkaBackoffException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO kafkaReadExceptionHandler(Exception e, WebRequest request) {
        e.fillInStackTrace();
        return new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Mesaj sunucusuna erişilemedi.",
                request.getDescription(false)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO globalExceptionHandler(Exception e, WebRequest request) {
        e.fillInStackTrace();
        return new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Internal server error",
                request.getDescription(false)
        );
    }
}
