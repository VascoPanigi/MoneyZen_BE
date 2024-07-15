package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.Role;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByRoleName(String roleName){
        return this.roleRepository.findByRoleName(roleName).orElseThrow(() -> new NotFoundException("Role with name " + roleName + " not found!"));
    }
}
