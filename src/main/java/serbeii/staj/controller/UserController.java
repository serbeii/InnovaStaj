package serbeii.staj.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import serbeii.staj.configuration.jwt.JwtUtils;
import serbeii.staj.dto.ErrorDTO;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.entity.User;
import serbeii.staj.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class UserController {
    // TODO: mesajları messages kısmına ekle
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            System.out.println("1");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("2");
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

            System.out.println("3");
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(loginRequest);

            System.out.println("4");
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            System.out.println("5");
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(userDetails);
        }
        catch (Exception e) {
            e.fillInStackTrace();
            ErrorDTO err = new ErrorDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    new Date(),
                    "Kullanıcı adı kullanımda.",
                    e.getMessage()
                    );
            return ResponseEntity.badRequest().body(err);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }
}
