package dmp.service.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PdfByteConverter
{
    public static ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName)
    {
        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try
        {
            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();
            
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) 
            {
                baos.write(buffer, 0, bytesRead);
            }
            
            baos.flush();
            baos.close();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (IOException e)
        {
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        
        return baos;
    }
}
