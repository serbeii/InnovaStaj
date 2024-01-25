package serbeii.staj.service;


import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.entity.User;
import serbeii.staj.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> register(UserDTO userDTO){
        Optional<User> byMail = userRepository.findByEmail(userDTO.getEmail());
        if (byMail.isPresent()) {
            return new ResponseEntity<>("mail", HttpStatus.CONFLICT);
        }
        Optional<User> byUsername = userRepository.findByUsername(userDTO.getUsername());
        if (byUsername.isPresent()) {
            return new ResponseEntity<>("username", HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        try{
            userRepository.save(user);
        }
        catch(Exception e){
            if (!e.getClass().equals(QueryException.class)) {
                e.fillInStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> login(UserDTO userDTO) {
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isPresent()) {
            boolean authenticate = passwordEncoder.matches(userDTO.getPassword(),
                    user.map(User::getPassword).orElse(null));
            if (authenticate){
                LoginDTO loginDTO = new LoginDTO();
                loginDTO.setUsername(user.map(User::getUsername).orElseThrow());
                loginDTO.setId(user.map(User::getId).orElseThrow());
                return new ResponseEntity<LoginDTO>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
