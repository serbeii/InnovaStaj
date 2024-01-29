package serbeii.staj.service;

import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;

public interface UserService {
    void register(UserDTO userDTO);
    LoginDTO login(UserDTO userDTO);
}
