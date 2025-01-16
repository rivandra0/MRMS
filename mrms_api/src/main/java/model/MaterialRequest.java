package model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialRequest {
    private Integer requestId;
    private String status;
    private String submitBy;
    private LocalDateTime submitDate;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String rejectedBy;
    private LocalDateTime rejectedDate;
    private List<MaterialRequestItem> items; // For manual or nested result mapping

}