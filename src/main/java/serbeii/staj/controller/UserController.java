package serbeii.staj.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO){
           userService.register(userDTO);
           return ResponseEntity.status(HttpStatus.CREATED)
                   .body("Hesap başarıyla oluşturuldu.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) {
            LoginDTO loginDTO = userService.login(userDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loginDTO);

    }

}
