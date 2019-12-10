package dmp.model.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class TransactionRowMapper implements RowMapper<Transaction>
{
    private static final Logger LOGGER = LogManager.getLogger(TransactionRowMapper.class);
    
    @Override
    public Transaction mapRow(ResultSet row, int rowNum)
    {
        Transaction tran = null;
        
        try
        {
            tran = new Transaction();
            tran.setId(row.getLong("id"));
            tran.setCaseId(row.getString("case_id"));
            tran.setRrnNo(row.getString("rrn_no"));
            tran.setPosId(row.getString("pos_id"));
            tran.setTerminalId(row.getString("terminal_id"));
            tran.setAmount(row.getString("amount"));
            tran.setMessageCode(row.getString("message_code"));
            tran.setMessageResponse(row.getString("message_response"));
            tran.setTranType(row.getString("tran_type"));
            tran.setFileName(row.getString("file_name"));
            tran.setCreatedAt(row.getTimestamp("created_at"));
        }
        catch (SQLException ex)
        {
            LOGGER.error(ex.toString());
        }
        
        return tran;
    }
}
