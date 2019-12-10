package dmp.service.user.impl;

import dmp.model.settings.Settings;
import dmp.model.settings.repository.SettingsRepository;
import dmp.model.user.Images;
import dmp.model.user.User;
import dmp.model.user.repository.ImagesRepository;
import dmp.model.user.repository.UserRepository;
import dmp.service.UtilServices;
import dmp.service.user.UserService;
import dmp.service.util.SHA;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService
{
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImagesRepository imagesRepository;
    
    @Autowired
    private SettingsRepository settingsRepository;
    
    @Autowired
    private UtilServices utilServices;
    
    @Override
    public String checkEmailDuplication(String email)
    {
        User userDB = userRepository.findByEmailAndIsDeletedFalse(email);
        
        String response = "";
        
        if(userDB != null)
        {
            response = "[{\"status\" : \"Email already exists\"}]";
        }
        else
        {
            response = "[{\"status\" : \"\"}]";
        }
        
        return response;
    }
    
    @Override
    public String checkMobileDuplication(String mobile)
    {
        User userDB = userRepository.findByMobileAndIsDeletedFalse(mobile);
        
        String response = "";
        
        if(userDB != null)
        {
            response = "[{\"status\" : \"Mobile already exists\"}]";
        }
        else
        {
            response = "[{\"status\" : \"\"}]";
        }
        
        return response;
    }
    
    @Override
    public String saveUserInfo(User user, MultipartFile userImage)
    {
        if(user.getMiddleName() == null)
        {
            user.setMiddleName(" ");
        }
        
        String salt = utilServices.getSalt(128);
        user.setSalt(salt);
        user.setPassword(SHA.encrypt("12345" + salt));
        user.setAddress(user.getAddress().replaceAll("(\r\n|\n)", "<br />"));
        user.setHasSequrityGroup(false);
        user.setDeleted(false);
        user.setEnabled(true);
        user.setIsPasswordReset(Boolean.FALSE);
        user.setIsPasswordExpired(Boolean.FALSE);
        user.setSuccessiveFailedLogins(0);
        user.setUpdatedAt(utilServices.getTodaysDate());
        user.setPasswordUpdatedOn(utilServices.getTodaysDate());
        
        if (userImage.getSize() > 0)
        {
            try
            {
                Images img = new Images();
                
                img.setFileName(userImage.getOriginalFilename());
                img.setFileContent(utilServices.getBlobData(userImage));
                
                java.util.Date today = new java.util.Date();
                
                user.setImage(imagesRepository.save(img));
            }
            catch (IOException | SQLException ex)
            {
                LOGGER.error("Message: " + ex.getMessage());
            }
        }
        
        String response = "";
        
        User userDB = userRepository.save(user);
        
        return response;
    }
    
    @Override
    public String updateUserInfo(User user, MultipartFile userImage, long userId, Boolean isEnabled, Boolean resetPassword)
    {
        Optional<User> optUser = userRepository.findById(userId);
        
        optUser.get().setFirstName(user.getFirstName());
        optUser.get().setMiddleName(user.getMiddleName());
        optUser.get().setLastName(user.getLastName());
        optUser.get().setMobile(user.getMobile());
        
        if(user.getAddress() != null)
        {
            optUser.get().setAddress(user.getAddress().replaceAll("(\r\n|\n)", "<br />"));
        }
        
        optUser.get().setNationalIdCardNo(user.getNationalIdCardNo());
        optUser.get().setDateOfBirth(user.getDateOfBirth());
        optUser.get().setRole(user.getRole());
        optUser.get().setEnabled(isEnabled);
        
        if(resetPassword)
        {
            //userDB.setUserPassword("12345");
            String salt = utilServices.getSalt(128);
            optUser.get().setSalt(salt);
            optUser.get().setPassword(SHA.encrypt("12345" + salt));
            optUser.get().setIsPasswordReset(Boolean.TRUE);
            optUser.get().setPasswordUpdatedOn(utilServices.getTodaysDate());
            optUser.get().setUpdatedAt(utilServices.getTodaysDate());
        }
        
        optUser.get().setUpdatedAt(utilServices.getTodaysDate());
        
        if (userImage.getSize() > 0)
        {
            try
            {
                Images img = new Images();
                
                img.setFileName(userImage.getOriginalFilename());
                img.setFileContent(utilServices.getBlobData(userImage));
                
                optUser.get().setImage(imagesRepository.save(img));
            }
            catch (IOException | SQLException ex)
            {
                LOGGER.error("Message: " + ex.getMessage());
            }
        }
        
        String response = "";
        
        if (userRepository.save(optUser.get()) != null)
        {
            response = "Updated successfully";
        }
        else
        {
            response = "Error occured";
        }
        
        return response;
    }
    
    @Override
    public String deleteUserInfo(long id)
    {
        String response = "";
        
        try
        {
            Optional<User> optUser = userRepository.findById(id);
            
            long imageId = 0;
            if(optUser.get().getImage() != null)
            {
                imageId = optUser.get().getImage().getId();
            }
            
            userRepository.delete(optUser.get());
            
            if(imageId != 0)
            {
                imagesRepository.delete(imagesRepository.findById(imageId).get());
            }
            
            response = "Deleted successfully";
        }
        catch(DataIntegrityViolationException e)
        {
            response = "Child record exists";
        }
        catch(Exception e)
        {
            response = "Error occured";
        }
        
        return response;
    }
    
    @Override
    public String saveNewPassword(String oldPassword, String newPassword, HttpServletRequest request)
    {
        String salt = utilServices.getSalt(128);
        String status = "";
        Settings settings = settingsRepository.findByKeyName("passwordMinCharLength");
        
        try {
            
            User user = (User) request.getSession().getAttribute("dmpparserusersession");
            
            if(user != null)
            {
                if(user.getPassword().equals(SHA.encrypt(oldPassword + user.getSalt())) == false)
                {
                    status = "Please enter valid current password";
                }
                else if(newPassword.length() < Integer.parseInt(settings.getKeyValue()))
                {
                    status = "New password require at least " + settings.getKeyValue() + " char";
                }
                else
                {
                    user.setSalt(salt);
                    user.setPassword(SHA.encrypt(newPassword + salt));
                    user.setIsPasswordReset(Boolean.TRUE);
                    user.setUpdatedAt(utilServices.getTodaysDate());
                    user.setPasswordUpdatedOn(utilServices.getTodaysDate());
                    
                    if(userRepository.save(user) != null)
                    {
                        status = "Password changed successfully";
                    }
                }
            }
            else
            {
                status = "Invalid user";
            }
        } catch (NumberFormatException ex)
        {
            LOGGER.error(ex.toString());
        }
        
        return status;
    }
}
