package serbeii.staj.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import serbeii.staj.entity.ERole;
import serbeii.staj.entity.Role;
import serbeii.staj.repository.RoleRepository;

@Component
public class SetupComponent {
    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void createRoles() {
        for (ERole erole:ERole.values()) {
            Role r = new Role(erole);
            roleRepository.findByName(erole).orElseGet(() -> roleRepository.save(r));
        }
    }
}
