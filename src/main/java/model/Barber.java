package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barber {
    private String id;
    private String name;
    private String phoneNumber;
    private Boolean isOwner;
    private Boolean isActive;
    private Object[] appointments;
    private String createdAt;
    private String updatedAt;

    public Barber(String name, String phoneNumber, Boolean isOwner, Boolean isActive) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isOwner = isOwner;
        this.isActive = isActive;
    }

}