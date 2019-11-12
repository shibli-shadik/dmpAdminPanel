package dmp.model.user.repository;

import dmp.model.user.User;
import dmp.model.user.UserLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends CrudRepository<UserLog, Long> {
    UserLog findByUserAndTokenAndIsLoggedInTrue(User user, String token);
    UserLog findByTokenAndIsLoggedInTrue(String token);
    UserLog findFirstByUserOrderByIdDesc(User user);
}