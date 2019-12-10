package dmp.model.reports;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileRegister
{
    private long id;
    private String fileName;
    private Timestamp fileModifiedAt;
    private long size;
    private int totalLine;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
