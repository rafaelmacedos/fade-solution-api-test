package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private String id;
    private ArrayList<Item> itemsList;
    private String barberId;
    private String customerId;
    private String barberName;
    private Double totalServicesValue;
    private Double totalCommissionValue;
    private String customerName;
    private String observation;
    private String paymentMethod;
    private String createdAt;
    private String updatedAt;
    private String status;

    public Appointment(ArrayList<Item> itemsList, String barberId, String customerId, String observation, String paymentMethod, String status) {
        this.itemsList = itemsList;
        this.barberId = barberId;
        this.customerId = customerId;
        this.observation = observation;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
}
