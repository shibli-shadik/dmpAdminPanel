package dmp.service.user;

import dmp.model.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String checkEmailDuplication(String email);
    String checkMobileDuplication(String mobile);
    String saveUserInfo(User user, MultipartFile userImage);
    String updateUserInfo(User user, MultipartFile userImage, long userId, Boolean isEnabled, Boolean resetPassword);
    String deleteUserInfo(long id);
}
