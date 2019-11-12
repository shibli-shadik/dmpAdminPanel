package dmp.controller.admin.login;

import javax.servlet.http.HttpServletRequest;
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
    @PostMapping("/home")
    public String validateLoginInfo(Model model, HttpServletRequest request,
            @RequestParam("email") String email,
            @RequestParam("password") String password) 
    {
        return "home/home";
    }
    
    @GetMapping("/home")
    public String getHomePageInfo(Model model, HttpServletRequest request) 
    {
        return "home/home";
    }
    
    @GetMapping("/logout")
    public String getLogoutInfo(Model model, HttpServletRequest request) 
    {
        //request.getSession().removeAttribute("itclificdciusrsession");
        
        return "index";
    }
}
