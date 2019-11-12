package dmp.service.user.impl;

import dmp.model.user.User;
import dmp.model.user.repository.UserRepository;
import dmp.service.user.LoginService;
import dmp.service.util.SHA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService
{
    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean validateLoginInfo(String email, String password)
    {
        User user = userRepository.findByEmailAndIsDeletedFalseAndIsEnabledTrue(email);
        
        //Check encrypted password
        Boolean isPasswordValid = false;
        if(user != null)
        {
            if(user.getPassword().equals(SHA.encrypt(password + user.getSalt())))
            {
                isPasswordValid = true;
            }
        }
        
        if (user != null && isPasswordValid == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
