package dmp.model.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class RejectedRowMapper implements RowMapper<Rejected>
{
    private static final Logger LOGGER = LogManager.getLogger(RejectedRowMapper.class);
    
    @Override
    public Rejected mapRow(ResultSet row, int rowNum)
    {
        Rejected data = null;
        
        try 
        {
            data = new Rejected();
            data.setId(row.getLong("id"));
            data.setCaseId(row.getString("case_id"));
            data.setRrnNo(row.getString("rrn_no"));
            data.setPosId(row.getString("pos_id"));
            data.setTerminalId(row.getString("terminal_id"));
            data.setAmount(row.getString("amount"));
            data.setPaymentDate(row.getString("payment_date"));
            data.setTranType(row.getString("tran_type"));
            data.setStatus(row.getString("status"));
            data.setFileName(row.getString("file_name"));
        } 
        catch (SQLException ex) 
        {
            LOGGER.error(ex.toString());
        }
        
        return data;
    }
}
