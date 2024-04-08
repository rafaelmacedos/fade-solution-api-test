package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private String id;
    private String barberId;
    private String barberName;
    private String description;
    private String expenseType;
    private Double totalValue;
    private String createdAt;
    private String updatedAt;

    public Expense(String barberId, String description, String expenseType, Double totalValue) {
        this.barberId = barberId;
        this.description = description;
        this.expenseType = expenseType;
        this.totalValue = totalValue;
    }

}