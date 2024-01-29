package serbeii.staj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter @Setter @NoArgsConstructor
public class UserDTO {
    @Email()
    private String email;
    private String username;
    private String password;
    private boolean emailLogin;
}
