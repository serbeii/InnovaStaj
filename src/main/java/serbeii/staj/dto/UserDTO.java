package serbeii.staj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserDTO {
    private String email;
    private String username;
    private String password;
}
