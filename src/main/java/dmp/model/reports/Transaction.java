package dmp.model.reports;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction
{
    private long id;
    private String caseId;
    private String rrnNo;
    private String posId;
    private String terminalId;
    private String amount;
    private String messageCode;
    private String messageResponse;
    private String tranType;
    private String fileName;
    private Timestamp createdAt;
}
