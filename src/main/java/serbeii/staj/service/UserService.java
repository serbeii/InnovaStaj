package serbeii.staj.service;

import org.springframework.http.ResponseEntity;
import serbeii.staj.dto.UserDTO;

public interface UserService {
    ResponseEntity<?> register(UserDTO userDTO);
    ResponseEntity<?> login(UserDTO userDTO);
}
