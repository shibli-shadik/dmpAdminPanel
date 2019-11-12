package dmp.model.user.repository;

import dmp.model.enums.RoleType;
import dmp.model.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByRoleType(RoleType roleType);
}
