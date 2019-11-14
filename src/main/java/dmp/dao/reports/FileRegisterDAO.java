package dmp.dao.reports;

import dmp.model.reports.FileRegister;
import dmp.model.reports.FileRegisterRowMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        String sql = "SELECT id, name, last_modified_at, size, total_line, status, created_at, updated_at FROM file_register ORDER BY id DESC LIMIT 200";
        
        RowMapper<FileRegister> rowMapper = new FileRegisterRowMapper();
        return this.jdbcTemplate.query(sql, rowMapper);
    }
}
