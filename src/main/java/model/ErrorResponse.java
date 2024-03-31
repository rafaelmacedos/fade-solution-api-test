package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String instant;
    private int status;
    private String message;
    private Object[] errors;

    public ErrorResponse(String instant, int status, String message) {
        this.instant = instant;
        this.status = status;
        this.message = message;
    }
}
