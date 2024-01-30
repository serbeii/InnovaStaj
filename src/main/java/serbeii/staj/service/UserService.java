package serbeii.staj.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;

@Service
public interface UserService extends UserDetailsService {
    void register(UserDTO userDTO);
    LoginDTO login(UserDTO userDTO);
}
