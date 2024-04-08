package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer quantityInStock;
    private Integer commissionPercentage;
    private String createdAt;
    private String updatedAt;

    public Product(String name, String brand, String description, Double price, Integer quantityInStock, Integer commissionPercentage) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.commissionPercentage = commissionPercentage;
    }
}
