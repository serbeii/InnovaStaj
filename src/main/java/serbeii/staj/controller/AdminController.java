package serbeii.staj.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serbeii.staj.configuration.jwt.JwtUtils;

@RequestMapping("/api/admin")
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true",
        allowedHeaders = "*", exposedHeaders = "set-cookie")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/test")
    public ResponseEntity<?> testAdmin(HttpServletRequest request) {
        System.out.println("tested " + jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request)));
        return  ResponseEntity.ok(jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request)));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getName() + " " + file.getContentType());
            System.out.println(file);
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e) {
            e.fillInStackTrace();
            return ResponseEntity.internalServerError().body(e);
        }
    }
}

