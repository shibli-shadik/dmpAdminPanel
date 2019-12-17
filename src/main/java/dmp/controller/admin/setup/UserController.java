package dmp.controller.admin.setup;

import dmp.model.enums.RoleType;
import dmp.model.settings.Settings;
import dmp.model.settings.repository.SettingsRepository;
import dmp.model.user.User;
import dmp.model.user.repository.RoleRepository;
import dmp.model.user.repository.UserRepository;
import dmp.service.user.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private SettingsRepository settingsRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping(value = "")
    public String getUsers(Model model, HttpServletRequest request)
    {
        request.getSession().setAttribute("dmpparsercurrenturl", "user");
        model.addAttribute("users", userRepository.findByIsDeletedFalse());
        
        return "user/userList";
    }
    
    @GetMapping("/createUser")
    public String createUser(Model model, HttpServletRequest request)
    {
        model.addAttribute("roles", roleRepository.findAll());
        
        return "user/createUser";
    }
    
    @PostMapping("/checkEmailDuplication")
    @ResponseBody
    public String checkEmailDuplication(HttpServletRequest request)
    {
        return userService.checkEmailDuplication(request.getParameter("email"));
    }
    
    @PostMapping("/checkMobileDuplication")
    @ResponseBody
    public String checkMobileDuplication(HttpServletRequest request)
    {
        return userService.checkMobileDuplication(request.getParameter("mobile"));
    }
    
    @PostMapping(value = "/save")
    public ModelAndView saveUserInfo(User user, BindingResult bindingResult,
            HttpServletRequest request, Model model,
            @RequestParam("userImage") MultipartFile userImage)
    {
        model.addAttribute("message", userService.saveUserInfo(user, userImage));
        model.addAttribute("users", userRepository.findByIsDeletedFalse());
        
        return new ModelAndView("user/userList");
    }
    
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable long id, Model model, HttpServletRequest request)
    {
        Optional<User> optUser = Optional.ofNullable(userRepository.findById(id).get());
        
        if(optUser.isPresent())
        {
            User user = userRepository.findById(id).get();
            
            if(user.getAddress() != null)
            {
                user.setAddress(user.getAddress().replace("<br />","\n"));
            }
            
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("user", user);
        }
        
        return "user/editUser";
    }
    
    @PostMapping("/update")
    public ModelAndView updateUserInfo(User user, BindingResult bindingResult,
            @RequestParam("userImage") MultipartFile userImage,
            @RequestParam("userId") long userId,
            @RequestParam("txtIsEnabled") Boolean isEnabled,
            @RequestParam("txtResetPassword") Boolean resetPassword,
            Model model) throws IOException, SQLException
    {
        
        model.addAttribute("message", userService.updateUserInfo(user, userImage, userId, isEnabled, resetPassword));
        model.addAttribute("users", userRepository.findByIsDeletedFalse());
        
        return new ModelAndView("user/userList");
    }
    
    @GetMapping("/{id}/delete")
    public ModelAndView deleteUserInfo(@PathVariable long id, Model model)
    {
        model.addAttribute("message", userService.deleteUserInfo(id));
        model.addAttribute("users", userRepository.findByIsDeletedFalse());
        
        return new ModelAndView("user/userList");
    }
    
    @GetMapping(value = "/{id}/view")
    public String viewDetails(@PathVariable long id, Model model, HttpServletRequest request)
    {
        User user= userRepository.findById(id).get();
        
        model.addAttribute("user", user);
        
        return "user/viewDetails";
    }
    
    @GetMapping("/userProfile")
    public String userProfile(Model model, HttpServletRequest request) 
    {
        request.getSession().setAttribute("dmpparsercurrenturl", "user/userProfile");
        User user = (User) request.getSession().getAttribute("dmpparserusersession");
        user = userRepository.findById(user.getId()).get();
        
        model.addAttribute("user", user);
        
        if(user.getRole().getRoleType() == RoleType.ADMIN)
        {
            return "user/userProfileAdmin";
        }
        else
        {
            return "user/userProfileUser";
        }
    }
    
    @GetMapping("/changePassword")
    public String changePassword(Model model, HttpServletRequest request) 
    {
        request.getSession().setAttribute("dmpparsercurrenturl", "user/changePassword");
        Settings settings = settingsRepository.findByKeyName("passwordMinCharLength");
        model.addAttribute("passwordMinCharLength", settings.getKeyValue());
        
        return "user/changePassword";
    }
    
    @PostMapping("/saveNewPassword")
    @ResponseBody
    public String saveNewPassword(HttpServletRequest request)
    {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        
        return "[{\"status\" : \"" + userService.saveNewPassword(oldPassword, newPassword, request) + "\"}]";
    }
}
