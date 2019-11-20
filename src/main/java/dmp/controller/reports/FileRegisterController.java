package dmp.controller.reports;

import dmp.model.Helper;
import dmp.model.enums.FileStatus;
import dmp.model.reports.FileRegister;
import dmp.model.reports.FileRegisterRowMapper;
import dmp.model.reports.SearchCmdFileRegister;
import dmp.service.UtilServices;
import dmp.service.reports.IFileRegisterService;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fileRegister")
public class FileRegisterController
{
    private static final Logger LOGGER = LogManager.getLogger(FileRegisterController.class);
    
    @Autowired
    private IFileRegisterService fileRegisterService;
    
    @Autowired
    private UtilServices utilServices;
    
    @GetMapping(value = "")
    public String getAllFiles(Model model, @ModelAttribute("searchCmdFileRegister") SearchCmdFileRegister cmdFileRegister)
    {
        model.addAttribute("searchCmdFileRegister", cmdFileRegister);
        
        List<FileRegister> list = fileRegisterService.getAllFiles();
        model.addAttribute("files", list);
        
        return "reports/fileRegister";
    }
    
    @GetMapping(value = "/search")
    public String searchRegisteredFileG(Model model, @ModelAttribute("searchCmdFileRegister") SearchCmdFileRegister cmdFileRegister)
    {
        model.addAttribute("searchCmdFileRegister", cmdFileRegister);
        
        List<FileRegister> list = fileRegisterService.getAllFiles();
        model.addAttribute("files", list);
        
        return "reports/fileRegister";
    }
    
    @PostMapping(value = "/search")
    public String searchRegisteredFile(Model model, @ModelAttribute("searchCmdFileRegister") SearchCmdFileRegister cmdFileRegister,
            @RequestParam Map<String, String> allRequestParams)
    {
        model.addAttribute("searchCmdFileRegister", cmdFileRegister);
        
        String strFromDate = allRequestParams.get("fromDate");
        String strToDate = allRequestParams.get("toDate");
        String strProcessStatus = allRequestParams.get("processStatus");
        
        String message = "";
        
        if(!"".equals(strFromDate) && !"".equals(strToDate))
        {
            try
            {
                if(utilServices.convertStringToDate(strFromDate, "yyyy-MM-dd").after(utilServices.convertStringToDate(strToDate, "yyyy-MM-dd")))
                {
                    message = "From date can't be greater then To date";
                    model.addAttribute("message", message);
                    return "reports/fileRegister";
                }
            }
            catch (ParseException ex)
            {
                LOGGER.error(ex.toString());
            }
        }
        else
        {
            if("".equals(strFromDate) && !"".equals(strToDate))
            {
                message = "Please enter from date";
                model.addAttribute("message", message);
                return "reports/fileRegister";
            }
            else if("".equals(strToDate) && !"".equals(strFromDate))
            {
                message = "Please enter to date";
                model.addAttribute("message", message);
                return "reports/fileRegister";
            }
        }
        
        if (Helper.EXPORT_PDF_ACTION.equals(allRequestParams.get("action")))
        {
            /*
            try
            {
            //pdfService.exportTopupDetails(allRequestParams, request, response);
            }
            catch (IOException ex)
            {
            LOGGER.error(ex.toString());
            }
            */
        }
        
        List<FileRegister> list = fileRegisterService.searchFileRegister(strFromDate, strToDate, strProcessStatus);
        model.addAttribute("files", list);
        
        return "reports/fileRegister";
    }
}
