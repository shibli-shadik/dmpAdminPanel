package dmp.service.reports;

import dmp.model.reports.Transaction;
import java.util.List;

public interface ITransactionService 
{
    List<Transaction> getAllTransaction();
    List<Transaction> searchTransaction(String strFromDate, String strToDate, String caseId, String rrnNo);
}
