package serbeii.staj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.service.UserService;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO){
       return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) {
        return userService.login(userDTO);
    }

}
