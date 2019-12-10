package dmp.model.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rejected
{
    private long id; 
    private String caseId; 
    private String rrnNo; 
    private String posId; 
    private String terminalId; 
    private String amount; 
    private String paymentDate; 
    private String tranType; 
    private String status; 
    private String fileName;
}
