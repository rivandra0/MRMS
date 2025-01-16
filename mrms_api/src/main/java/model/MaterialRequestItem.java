package model;

import lombok.Data;

@Data
public class MaterialRequestItem {
    private Integer itemId;
    private Integer requestId;
    private String materialName;
    private Integer requestedQuantity;
    private String usageDescription;
}
