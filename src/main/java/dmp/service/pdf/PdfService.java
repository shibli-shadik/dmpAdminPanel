package dmp.service.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class PdfService 
{
    private static final Logger LOGGER = LogManager.getLogger(PdfService.class);
    
    @Autowired
    private PdfCreatorFileRegister pdfCreatorFileRegister;
    
        public void exportRegisteredFiles(Map<String, String> allRequestParams, HttpServletRequest request,
            HttpServletResponse response)
        {
        
        final ServletContext servletContext = request.getSession().getServletContext();
        final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        final String temperotyFilePath = tempDirectory.getAbsolutePath();
        
        String fileName = "RegisteredFiles.pdf";
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        OutputStream os = null;
        try 
        {
            pdfCreatorFileRegister.createPDF(temperotyFilePath + "\\" + fileName, allRequestParams, request);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos = PdfByteConverter
                    .convertPDFToByteArrayOutputStream(temperotyFilePath + "\\"
                            + fileName);
            
            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } 
        catch (Exception ex) 
        {
            LOGGER.error(ex.toString());
        }
    }
}
