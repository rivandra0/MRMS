package model;

import lombok.Data;

@Data
public class MaterialRequestItem {
    private String itemId;
    private String requestId;
    private String materialName;
    private int requestedQuantity;
    private String usageDescription;
}
