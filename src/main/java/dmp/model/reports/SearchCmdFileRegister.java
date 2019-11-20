package dmp.model.reports;

import dmp.model.enums.FileStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCmdFileRegister
{
    private String fromDate;
    private String toDate;
    private FileStatus processStatus;
}
