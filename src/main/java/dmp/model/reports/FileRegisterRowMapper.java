package dmp.model.reports;

import dmp.model.enums.FileStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class FileRegisterRowMapper implements RowMapper<FileRegister>
{
    private static final Logger LOGGER = LogManager.getLogger(FileRegisterRowMapper.class);
    
    @Override
    public FileRegister mapRow(ResultSet row, int rowNum)
    {
        FileRegister file = null;
        
        try 
        {
            file = new FileRegister();
            file.setId(row.getLong("id"));
            file.setFileName(row.getString("name"));
            file.setFileModifiedAt(row.getTimestamp("last_modified_at"));
            file.setSize(row.getLong("size"));
            file.setTotalLine(row.getInt("total_line"));
            file.setStatus(FileStatus.values()[row.getInt("status")].getDisplayName());
            file.setCreatedAt(row.getTimestamp("created_at"));
            file.setUpdatedAt(row.getTimestamp("updated_at"));
        } 
        catch (SQLException ex) 
        {
            LOGGER.error(ex.toString());
        }
        
        return file;
    }
}
