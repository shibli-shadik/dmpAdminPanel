package dmp.service.reports;

import dmp.dao.reports.ITransactionDAO;
import dmp.model.reports.Transaction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService
{
    
    @Autowired
    private ITransactionDAO transactionDAO;
    
    @Override
    public List<Transaction> getAllTransaction()
    {
        return transactionDAO.getAllTransaction();
    }

    @Override
    public List<Transaction> searchTransaction(String strFromDate, String strToDate, String caseId, String rrnNo) 
    {
        return transactionDAO.searchTransaction(strFromDate, strToDate, caseId, rrnNo);
    }
}
