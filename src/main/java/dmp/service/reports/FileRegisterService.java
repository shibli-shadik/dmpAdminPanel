package dmp.service.reports;

import dmp.dao.reports.IFileRegisterDAO;
import dmp.model.reports.FileRegister;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileRegisterService implements IFileRegisterService
{
    @Autowired
    private IFileRegisterDAO fileRegisterDAO;
    
    @Override
    public List<FileRegister> getAllFiles()
    {
        return fileRegisterDAO.getAllFiles();
    }
}
