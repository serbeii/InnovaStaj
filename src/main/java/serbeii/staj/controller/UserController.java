package serbeii.staj.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serbeii.staj.configuration.jwt.JwtUtils;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.service.UserService;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true",
        allowedHeaders = "*", exposedHeaders = "set-cookie")
@RestController
@RequestMapping("/api/auth")
public class UserController {
    // TODO: mesajları messages kısmına ekle
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Hesap başarıyla oluşturuldu.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) {
        LoginDTO loginDTO = userService.login(userDTO);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDTO);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(loginDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }

    @GetMapping("/validateLogin")
    public ResponseEntity<?> validateLogin(HttpServletRequest request) {
        boolean valid = jwtUtils.validateJwtToken(jwtUtils.getJwtFromCookies(request));
        if (valid) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
