package dmp.dao.reports;

import dmp.model.reports.Transaction;
import dmp.model.reports.TransactionRowMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class TransactionDAO implements ITransactionDAO
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Transaction> getAllTransaction()
    {
        String sql = "SELECT a.id, a.case_id, a.rrn_no, a.pos_id, a.terminal_id, a.amount, a.message_code, a.message_response, a.tran_type, b.name file_name, a.created_at "
                + "FROM transactions a, file_register b "
                + "WHERE a.file_id = b.id "
                + "ORDER BY a.id DESC "
                + "LIMIT 200";
        
        RowMapper<Transaction> rowMapper = new TransactionRowMapper();
        return this.jdbcTemplate.query(sql, rowMapper);
    }
    
    @Override
    public List<Transaction> searchTransaction(String strFromDate, String strToDate, String caseId, String rrnNo)
    {
        String sql = "";
        String condDate = "";
        String condCaseId = "";
        String condRrnNo = "";
        
        List<Transaction> list = new ArrayList<>();
        RowMapper<Transaction> rowMapper = new TransactionRowMapper();
        
        if(!"".equals(strFromDate) && !"".equals(strToDate))
        {
            condDate = "AND a.created_at BETWEEN '" +  strFromDate + " 00:00:00'" + " AND '" + strToDate + " 23:59:59' ";
        }
        
        if(!"".equals(caseId))
        {
            condCaseId = "AND a.case_id = " + caseId + " ";
        }
        
        if(!"".equals(rrnNo))
        {
            condRrnNo = "AND a.rrn_no = " + rrnNo + " ";
        }
        
        sql = "SELECT a.id, a.case_id, a.rrn_no, a.pos_id, a.terminal_id, a.amount, a.message_code, a.message_response, a.tran_type, b.name file_name, a.created_at "
                + "FROM transactions a, file_register b "
                + "WHERE a.file_id = b.id "
                + condDate
                + condCaseId
                + condRrnNo
                + "ORDER BY a.id DESC";
        
        list = jdbcTemplate.query(sql, rowMapper);
        
        return list;
    }
}
