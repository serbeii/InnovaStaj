package serbeii.staj.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.service.UserService;
import serbeii.staj.service.exceptions.EmailTakenException;
import serbeii.staj.service.exceptions.PasswordMatchError;
import serbeii.staj.service.exceptions.UsernameTakenException;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO){
       try{
           userService.register(userDTO);
           return ResponseEntity.status(HttpStatus.CREATED)
                   .body("Hesap başarıyla oluşturuldu.");
       }
       catch (EmailTakenException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Eposta adresi kullanımda.");
       }
       catch (UsernameTakenException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Kullanıcı adı kullanımda.");
       }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) {
        try {
            LoginDTO loginDTO = userService.login(userDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loginDTO);
        }
        catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Kullanıcı bulunamadı.");
        }
        catch (PasswordMatchError e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Kullanıcı adı veya şifre hatalı.");
        }
    }

}
