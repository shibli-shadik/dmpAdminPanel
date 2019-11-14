package dmp.controller.reports;

import dmp.model.reports.FileRegister;
import dmp.service.reports.IFileRegisterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fileRegister")
public class FileRegisterController 
{
    @Autowired
    private IFileRegisterService fileRegisterService;
    
    @GetMapping(value = "")
    public String getAllFiles(Model model) 
    {
        List<FileRegister> list = fileRegisterService.getAllFiles();
        model.addAttribute("files", list);
        
        return "reports/fileRegister";
    }
}
