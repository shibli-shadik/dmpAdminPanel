package dmp.controller.admin.login;

import dmp.model.enums.RoleType;
import dmp.model.user.User;
import dmp.model.user.repository.UserRepository;
import dmp.service.user.LoginService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"/",""})
public class LoginController 
{
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private LoginService loginService;
        
    @PostMapping("/home")
    public String validateLoginInfo(Model model, HttpServletRequest request,
            @RequestParam("email") String email,
            @RequestParam("password") String password) 
    {
        if (loginService.validateLoginInfo(email, password) == true)
        {
            User user = userRepository.findByEmailAndIsDeletedFalseAndIsEnabledTrue(email);
            request.getSession().setAttribute("dmpparserusersession", user);
            model.addAttribute("user", user);
            
            return "/home/home";
        }
        else
        {
            model.addAttribute("message", "Wrong e-mail and password.");
            return "index";
        }
    }
    
    @GetMapping("/home")
    public String getHomePageInfo(Model model, HttpServletRequest request) 
    {
        return "home/home";
    }
    
    @GetMapping("/logout")
    public String getLogoutInfo(Model model, HttpServletRequest request) 
    {
        request.getSession().removeAttribute("dmpparserusersession");
        
        return "index";
    }
}
