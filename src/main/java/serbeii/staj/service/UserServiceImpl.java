package serbeii.staj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.entity.User;
import serbeii.staj.exception.UserNotFoundException;
import serbeii.staj.repository.UserRepository;
import serbeii.staj.exception.EmailTakenException;
import serbeii.staj.exception.PasswordMatchError;
import serbeii.staj.exception.UsernameTakenException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(UserDTO userDTO) {
        Optional<User> byMail = userRepository.findByEmail(userDTO.getEmail());
        if (byMail.isPresent()) {
            throw new EmailTakenException();
        }
        Optional<User> byUsername = userRepository.findByUsername(userDTO.getUsername());
        if (byUsername.isPresent()) {
            throw new UsernameTakenException();
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        userRepository.save(user);
    }

    @Override
    public LoginDTO login(UserDTO userDTO) {
        Optional<User> user;
        if (userDTO.isEmailLogin()) {
            user = userRepository.findByEmail(userDTO.getUsername());
        } else {
            user = userRepository.findByUsername(userDTO.getUsername());
        }
        boolean authenticate = passwordEncoder.matches(userDTO.getPassword(),
                user.map(User::getPassword).orElseThrow(UserNotFoundException::new));
        if (authenticate) {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(user.map(User::getUsername).orElseThrow());
            loginDTO.setId(user.map(User::getId).orElseThrow());
            return loginDTO;
        } else {
            throw new PasswordMatchError();
        }
    }
}

