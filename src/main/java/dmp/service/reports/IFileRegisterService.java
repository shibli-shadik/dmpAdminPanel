package dmp.service.reports;

import dmp.model.reports.FileRegister;
import java.util.List;

public interface IFileRegisterService 
{
    List<FileRegister> getAllFiles();
    List<FileRegister> searchFileRegister(String strFromDate, String strToDate, String status);
}
