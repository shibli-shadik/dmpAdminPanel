package dmp.controller.reports;

import dmp.model.Helper;
import dmp.model.reports.SearchCmdTransaction;
import dmp.model.reports.Transaction;
import dmp.service.UtilServices;
import dmp.service.pdf.PdfService;
import dmp.service.reports.ITransactionService;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/transactions")
public class TransactionController
{
    private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);
    
    @Autowired
    private ITransactionService transactionService;
    
    @Autowired
    private UtilServices utilServices;
    
    @Autowired
    private PdfService pdfService;
    
    @GetMapping(value = "")
    public String getAllTransaction(Model model, HttpServletRequest request, @ModelAttribute("searchCmdTransaction") SearchCmdTransaction cmdTransaction)
    {
        request.getSession().setAttribute("dmpparsercurrenturl", "transactions");
        model.addAttribute("searchCmdTransaction", cmdTransaction);
        
        List<Transaction> list = transactionService.getAllTransaction();
        model.addAttribute("transactions", list);
        
        return "reports/transactions";
    }
    
    @GetMapping(value = "/search")
    public String searchTransactionG(Model model, @ModelAttribute("searchCmdTransaction") SearchCmdTransaction cmdTransaction)
    {
        model.addAttribute("searchCmdTransaction", cmdTransaction);
        
        List<Transaction> list = transactionService.getAllTransaction();
        model.addAttribute("transactions", list);
        
        return "reports/transactions";
    }
    
    @PostMapping(value = "/search")
    public String searchTransaction(Model model, @ModelAttribute("searchCmdTransaction") SearchCmdTransaction cmdTransaction,
            @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request, HttpServletResponse response)
    {
        model.addAttribute("searchCmdTransaction", cmdTransaction);
        
        String strFromDate = allRequestParams.get("fromDate");
        String strToDate = allRequestParams.get("toDate");
        String caseId = allRequestParams.get("caseId");
        String rrnNo = allRequestParams.get("rrnNo");
        
        String message = "";
        
        if(!"".equals(strFromDate) && !"".equals(strToDate))
        {
            try
            {
                if(utilServices.convertStringToDate(strFromDate, "yyyy-MM-dd").after(utilServices.convertStringToDate(strToDate, "yyyy-MM-dd")))
                {
                    message = "From date can't be greater then To date";
                    model.addAttribute("message", message);
                    return "reports/transactions";
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
                return "reports/transactions";
            }
            else if("".equals(strToDate) && !"".equals(strFromDate))
            {
                message = "Please enter to date";
                model.addAttribute("message", message);
                return "reports/transactions";
            }
        }
        
        if (Helper.EXPORT_PDF_ACTION.equals(allRequestParams.get("action")))
        {
            try
            {
                pdfService.exportTransactions(allRequestParams, request, response);
            }
            catch (Exception ex)
            {
                LOGGER.error(ex.toString());
            }
        }
        
        List<Transaction> list = transactionService.searchTransaction(strFromDate, strToDate, caseId, rrnNo);
        model.addAttribute("transactions", list);
        
        return "reports/transactions";
    }
}
