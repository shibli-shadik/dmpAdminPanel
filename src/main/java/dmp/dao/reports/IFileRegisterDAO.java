package dmp.dao.reports;

import dmp.model.reports.FileRegister;
import java.util.List;

public interface IFileRegisterDAO 
{
    List<FileRegister> getAllFiles();
    List<FileRegister> searchFileRegister(String strFromDate, String strToDate, String status);
}
