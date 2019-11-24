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
import dmp.model.reports.FileRegister;
import dmp.model.user.User;
import dmp.service.UtilServices;
import dmp.service.reports.IFileRegisterService;
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
public class PdfCreatorFileRegister
{
    @Autowired
    private IFileRegisterService fileRegisterService;

    @Autowired
    private UtilServices utilServices;
    
    private static final Logger LOGGER = LogManager.getLogger(PdfCreatorFileRegister.class);
    
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
            
            String strProcessStatus = "";
            
            if(allRequestParams.get("processStatus") != null) 
            {
                strProcessStatus = allRequestParams.get("processStatus");
            }
            
            preface.add(new Paragraph("Registered Files Report", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
            document.add(preface);
            
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            
            if(!"".equals(strFromDate) && !"".equals(strToDate))
            {
                try
                {
                    table.addCell(getCell(
                            "Date : " + simpleDateFormat.format(utilServices.convertStringToDate(strFromDate, "yyyy-MM-dd")) + " to " + simpleDateFormat.format(utilServices.convertStringToDate(strToDate, "yyyy-MM-dd")),
                            PdfPCell.ALIGN_LEFT));
                }
                catch (ParseException ex)
                {
                    LOGGER.error(ex.toString());
                }
            }
            
            if(!"".equals(strProcessStatus))
            {
                table.addCell(getCell("Process Status : " + strProcessStatus, PdfPCell.ALIGN_LEFT));
            }
            
            table.addCell(getCell("Printed on : " + simpleDateFormat.format(new Date()), PdfPCell.ALIGN_LEFT));
            
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
            
            PdfPTable table = new PdfPTable(new float[] { (float) .6, (float) 2.1, (float) 1.3, (float) .7,
                (float).7, (float) .7, (float) 1.3, (float) 1.3});
            BaseColor backgroundColor = BaseColor.LIGHT_GRAY;
            
            table = createTableHeader(table, backgroundColor);
            
            String strFromDate = allRequestParams.get("fromDate");
            String strToDate = allRequestParams.get("toDate");
            
            String strProcessStatus = "";
            
            if(allRequestParams.get("processStatus") != null)
            {
                strProcessStatus = allRequestParams.get("processStatus");
            }
            
            List<FileRegister> list = null;
            list = fileRegisterService.searchFileRegister(strFromDate, strToDate, strProcessStatus);

            for (FileRegister item : list) 
            {
                table.setWidthPercentage(100);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                
                table.getDefaultCell().setPaddingTop(5);
                table.getDefaultCell().setPaddingRight(5);
                table.getDefaultCell().setPaddingBottom(7);
                table.getDefaultCell().setPaddingLeft(5);

                
                PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(item.getId()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c1);
                
                table.addCell(new PdfPCell(new Phrase(item.getFileName(), FONT_TABLE_BODY)));
                
                String fileModifiedAt = String.valueOf(item.getFileModifiedAt());
                table.addCell(new PdfPCell(new Phrase(fileModifiedAt.substring(0, fileModifiedAt.length()-2), FONT_TABLE_BODY)));
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getSize()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase(String.valueOf(item.getTotalLine()), FONT_TABLE_BODY));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c1);
                
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getStatus()), FONT_TABLE_BODY)));
                
                String createdAt = String.valueOf(item.getCreatedAt());
                table.addCell(new PdfPCell(new Phrase(createdAt.substring(0,createdAt.length()-2), FONT_TABLE_BODY)));
                
                String updatedAt = String.valueOf(item.getUpdatedAt());
                table.addCell(new PdfPCell(new Phrase(updatedAt.substring(0, updatedAt.length()-2), FONT_TABLE_BODY)));
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
        PdfPCell c1 = new PdfPCell(new Phrase("Id", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("File Name", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("File Creation Date", FONT_TABLE_HEADER));
        c1.setBackgroundColor(backgroundColor);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Size", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Total Line", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Status", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Created at", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Modified at", FONT_TABLE_HEADER));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        table.addCell(c1);
        
        table.setHeaderRows(1);
        
        return table;
    }
}
