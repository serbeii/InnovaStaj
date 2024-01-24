package serbeii.staj.service;


import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public boolean register(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        try{
            userRepository.save(user);
        }
        catch(Exception e){
            if (!e.getClass().equals(QueryException.class)) {
                e.fillInStackTrace();
            }
            return false;
        }
        return user.getId() > -1;
    }

    @Override
    public boolean login(UserDTO userDTO) {
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isPresent()) {
            boolean authenticate = passwordEncoder.matches(userDTO.getPassword(),
                    user.map(User::getPassword).orElse(null));
            System.out.println(user);
            return authenticate;
        }
        else {
            return false;
        }
    }
}
