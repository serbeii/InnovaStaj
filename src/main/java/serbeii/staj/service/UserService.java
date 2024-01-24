package serbeii.staj.service;

import serbeii.staj.dto.UserDTO;

public interface UserService {
    boolean register(UserDTO userDTO);
    boolean login(UserDTO userDTO);
}
