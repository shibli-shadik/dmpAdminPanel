package dmp.dao.reports;

import dmp.model.enums.FileStatus;
import dmp.model.reports.FileRegister;
import dmp.model.reports.FileRegisterRowMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class FileRegisterDAO implements IFileRegisterDAO
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<FileRegister> getAllFiles()
    {
        String sql = "SELECT id, name, last_modified_at, size, total_line, status, created_at, updated_at "
                + "FROM file_register "
                + "ORDER BY id DESC "
                + "LIMIT 200";
        
        RowMapper<FileRegister> rowMapper = new FileRegisterRowMapper();
        return this.jdbcTemplate.query(sql, rowMapper);
    }
    
    @Override
    public List<FileRegister> searchFileRegister(String strFromDate, String strToDate, String status)
    {
        String sql = "";
        List<FileRegister> list = new ArrayList<>();
        RowMapper<FileRegister> rowMapper = new FileRegisterRowMapper();
        
        if(!"".equals(strFromDate) && !"".equals(strToDate) && !"".equals(status))
        {
            sql = "SELECT id, name, last_modified_at, size, total_line, status, created_at, updated_at "
                + "FROM file_register "
                + "WHERE status = ? "
                + "AND created_at BETWEEN " +  "?" + " AND " + "? "
                + "ORDER BY id DESC";
            
            list = jdbcTemplate.query(sql, rowMapper, FileStatus.valueOf(status).ordinal(), strFromDate + " 00:00:00", strToDate + " 23:59:59");
        }
        else if(!"".equals(strFromDate) && !"".equals(strToDate) && "".equals(status))
        {
            sql = "SELECT id, name, last_modified_at, size, total_line, status, created_at, updated_at "
                + "FROM file_register "
                + "WHERE created_at BETWEEN " +  "?" + " AND " + "? "
                + "ORDER BY id DESC";
            
            list = jdbcTemplate.query(sql, rowMapper, strFromDate + " 00:00:00", strToDate + " 23:59:59");
        }
        else if("".equals(strFromDate) && "".equals(strToDate) && !"".equals(status))
        {
            sql = "SELECT id, name, last_modified_at, size, total_line, status, created_at, updated_at "
                + "FROM file_register "
                + "WHERE status = ? "
                + "ORDER BY id DESC";
            
            list = jdbcTemplate.query(sql, rowMapper, FileStatus.valueOf(status).ordinal());
        }
        
        return list;
    }
}
