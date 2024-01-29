package serbeii.staj.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserDTO {
    @Email
    private String email;
    private String username;
    private String password;
    private boolean emailLogin;
}
