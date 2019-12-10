package dmp.dao.reports;

import dmp.model.reports.Transaction;
import java.util.List;

public interface ITransactionDAO 
{
    List<Transaction> getAllTransaction();
    List<Transaction> searchTransaction(String strFromDate, String strToDate, String caseId, String rrnNo);
}
