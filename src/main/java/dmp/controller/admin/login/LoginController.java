package dmp.controller.admin.login;

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
           
            //Redirect to last visited url
            String strUrl = "";
            
            if(request.getSession().getAttribute("dmpparsercurrenturl") == null)
            {
                strUrl = "/home/home";
                request.getSession().setAttribute("dmpparsercurrenturl", "home");
            }
            else
            {
                strUrl = "redirect:/" + request.getSession().getAttribute("dmpparsercurrenturl").toString();
            }
            request.getSession().setMaxInactiveInterval(60*5);
            
            model.addAttribute("user", user);
            
            //return "/home/home";
            return strUrl;
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
        request.getSession().setAttribute("dmpparsercurrenturl", "home");
        return "home/home";
    }
    
    @GetMapping("/logout")
    public String getLogoutInfo(Model model, HttpServletRequest request) 
    {
        request.getSession().removeAttribute("dmpparserusersession");
        request.getSession().removeAttribute("dmpparsercurrenturl");
        
        return "index";
    }
}
