package model;

import lombok.Data;

@Data
public class CommonDTO {
    private String status;
    private String message;
    private Object data;
}
