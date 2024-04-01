package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferedService {
    private String id;
    private String name;
    private String description;
    private Double value;
    private String createdAt;
    private String updatedAt;

    public OfferedService(String name, String description, Double value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
