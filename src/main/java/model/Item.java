package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String brand;
    private Integer soldQuantity;
    private Double price;

    public Item(OfferedService offeredService, Integer soldQuantity) {
        this.id = offeredService.getId();
        this.name = offeredService.getName();
        this.price = offeredService.getValue();
        this.soldQuantity = soldQuantity;
    }

    public Item(Product product, Integer soldQuantity) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.price = product.getPrice();
        this.soldQuantity = soldQuantity;
    }

}
