package dmp.model.reports;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCmdTransaction
{
    private String fromDate;
    private String toDate;
    private String caseId;
    private String rrnNo;
}
