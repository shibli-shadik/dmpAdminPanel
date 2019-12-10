package dmp.service.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dmp.model.reports.Transaction;
import dmp.model.user.User;
import dmp.service.UtilServices;
import dmp.service.reports.ITransactionService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfCreatorTransactions
{
    @Autowired
    private ITransactionService transactionService;
    
    @Autowired
    private UtilServices utilServices;
    
    private static final Logger LOGGER = LogManager.getLogger(PdfCreatorTransactions.class);
    
    private static final Font FONT_TABLE_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
    private static final Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
    
    public Document createPDF(String file, Map<String, String> allRequestParams, HttpServletRequest request)
    {
        Document document = null;
        
        try
        {
            document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            
            addMetaData(document, request);
            addTitlePage(document, allRequestParams);
            createTable(document, allRequestParams);
            
            document.close();
            
        }
        catch (FileNotFoundException | DocumentException ex)
        {
            LOGGER.error(ex.toString());
        }
        
        return document;
    }
    
    private static void addMetaData(Document document, HttpServletRequest request)
    {
        User loggedInUser = (User) request.getSession().getAttribute("dmpparserusersession");
        
        document.addTitle("Generate PDF report");
        document.addSubject("Generate PDF report");
        
        if (loggedInUser != null)
        {
            document.addAuthor(loggedInUser.getDisplayName());
            document.addCreator(loggedInUser.getDisplayName());
        }
    }
    
    private void addTitlePage(Document document, Map<String, String> allRequestParams)
    {
        try
        {
            Paragraph preface = new Paragraph();
            
            String strFromDate = allRequestParams.get("fromDate");
            String strToDate = allRequestParams.get("toDate");
            
            String strCaseId = "";
            String strRrnNo = "";
            
            if(allRequestParams.get("caseId") != null)
            {
                strCaseId = allRequestParams.get("caseId");
            }
            
            if(allRequestParams.get("rrnNo") != null)
            {
                strRrnNo = allRequestParams.get("rrnNo");
            }
            
            preface.add(new Paragraph("Transactions Report", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
            document.add(preface);
            
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            
            if(!"".equals(strFromDate) && !"".equals(strToDate))
            {
                try
                {
                    table.addCell(getCell(
                            "Date: " + simpleDateFormat.format(utilServices.convertStringToDate(strFromDate, "yyyy-MM-dd")) + " to " + simpleDateFormat.format(utilServices.convertStringToDate(strToDate, "yyyy-MM-dd")),
                            PdfPCell.ALIGN_LEFT));
                }
                catch (ParseException ex)
                {
                    LOGGER.error(ex.toString());
                }
            }
            
            if(!"".equals(strCaseId))
            {
                table.addCell(getCell("Case Id: " + strCaseId, PdfPCell.ALIGN_LEFT));
            }
            
            if(!"".equals(strRrnNo))
            {
                table.addCell(getCell("RRN No: " + strRrnNo, PdfPCell.ALIGN_LEFT));
            }
            
            table.addCell(getCell("Printed on: " + simpleDateFormat.format(new Date()), PdfPCell.ALIGN_LEFT));
            
            document.add(table);
        }
        catch (DocumentException ex)
        {
            LOGGER.error(ex.toString());
        }
    }
    
    public static PdfPCell getCell(String text, int alignment)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TABLE_HEADER));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        
        return cell;
    }
    
    private static void creteEmptyLine(Paragraph paragraph, int number)
    {
        for (int i = 0; i < number; i++)
        {
            paragraph.add(new Paragraph(" "));
        }
    }
    
    private void createTable(Document document, Map<String, String> allRequestParams)
    {
        try
        {
            Paragraph paragraph = new Paragraph();
            
            creteEmptyLine(paragraph, 1);
            document.add(paragraph);
            
            PdfPTable table = new PdfPTable(new float[] { (float) .9, (float) .9, (float) .6, (float) 2.5,
                (float).4, (float) 1.6, (float) 1.1});
            BaseColor backgroundColor = BaseColor.LIGHT_GRAY;
            
            table = createTableHeader(table, backgroundColor);
            
            String strFromDate = allRequestParams.get("fromDate");
            String strToDate = allRequestParams.get("toDate");
            
            String strCaseId = "";
            String strRrnNo = "";
            
            if(allRequestParams.get("caseId") != null)
            {
                strCaseId = allRequestParams.get("caseId");
            }
            
            if(allRequestParams.get("rrnNo") != null)
            {
                strRrnNo = allRequestParams.get("rrnNo");
            }
            
            List<Transaction> list = null;
            list = transactionService.searchTransaction(strFromDate, strToDate, strCaseId, strRrnNo);
            
            for (Transaction item : list)
            {
                table.setWidthPercentage(100);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                
                table.getDefaultCell().setPaddingTop(5);
                table.getDefaultCell().setPaddingRight(5);
                table.getDefaultCell().setPaddingBottom(7);
                table.getDefaultCell().setPaddingLeft(5);
                
                
                PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(item.getCaseId()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getRrnNo()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getAmount()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getMessageResponse()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getTranType()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getFileName()), FONT_TABLE_BODY)));
                
                String createdAt = String.valueOf(item.getCreatedAt());
                table.addCell(new PdfPCell(new Phrase(createdAt.substring(0,createdAt.length()-2), FONT_TABLE_BODY)));
            }
            
            document.add(table);
        }
        catch (DocumentException ex)
        {
            LOGGER.error(ex.toString());
        }
    }
    
    private static PdfPTable createTableHeader(PdfPTable table, BaseColor backgroundColor)
    {
        PdfPCell c1 = new PdfPCell(new Phrase("Case Id", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Rrn No", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Amount", FONT_TABLE_HEADER));
        c1.setBackgroundColor(backgroundColor);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Response", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Tran Type", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("File Name", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Created at", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        table.setHeaderRows(1);
        
        return table;
    }
}
