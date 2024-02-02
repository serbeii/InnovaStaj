package serbeii.staj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import serbeii.staj.dto.LoginDTO;
import serbeii.staj.dto.UserDTO;
import serbeii.staj.entity.ERole;
import serbeii.staj.entity.Role;
import serbeii.staj.entity.User;
import serbeii.staj.exception.EmailTakenException;
import serbeii.staj.exception.UserNotFoundException;
import serbeii.staj.exception.UsernameTakenException;
import serbeii.staj.repository.RoleRepository;
import serbeii.staj.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

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
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public LoginDTO login(UserDTO userDTO) {
        Optional<User> userOpt;
        if (userDTO.isEmailLogin()) {
            userOpt = userRepository.findByEmail(userDTO.getUsername());
        } else {
            userOpt = userRepository.findByUsername(userDTO.getUsername());
        }
        passwordEncoder.matches(userDTO.getPassword(),
                userOpt.map(User::getPassword).orElseThrow(UserNotFoundException::new));
        User user = userOpt.orElseThrow(UserNotFoundException::new);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> authorities = getUserRolesString(user);
        return new LoginDTO(
                user.getId(),
                user.getUsername(),
                authorities
        );
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username));
        List<SimpleGrantedAuthority> authorities = user.map(this::getUserRoles
        ).orElseThrow(UserNotFoundException::new);
        return new org.springframework.security.core.userdetails.User(
                user.map(User::getUsername).orElseThrow(UserNotFoundException::new),
                user.map(User::getPassword).orElseThrow(UserNotFoundException::new),
                authorities
        );
    }

    private List<SimpleGrantedAuthority> getUserRoles(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    private List<String> getUserRolesString(User user) {
        return user.getRoles().stream()
                .map(role -> role.getName().name()).collect(Collectors.toList());
    }
}
