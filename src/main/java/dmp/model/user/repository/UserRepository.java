package dmp.model.user.repository;

import dmp.model.user.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailAndIsDeletedFalse(String emailAddress);
    User findByEmailAndIsDeletedFalseAndIsEnabledTrue(String email);
    User findByMobileAndIsDeletedFalseAndIsEnabledTrue(String mobile);
    User findByMobileAndIsDeletedFalse(String mobile);
    List<User> findByIsDeletedFalse();
    List<User> findByIsDeletedFalseAndIsEnabledTrue();
}