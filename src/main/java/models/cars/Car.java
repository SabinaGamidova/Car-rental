package models.cars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Mappable;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;
    @Mappable(columnNumber = 2, dataType = String.class)
    private String number;
    @Mappable(columnNumber = 3, dataType = String.class)
    private String brand;
    @Mappable(columnNumber = 4, dataType = String.class)
    private String model;
    @Mappable(columnNumber = 5, dataType = UUID.class)
    private UUID carTypeId;
    @Mappable(columnNumber = 6, dataType = UUID.class)
    private UUID comfortId;
    @Mappable(columnNumber = 7, dataType = UUID.class)
    private UUID engineId;
    @Mappable(columnNumber = 8, dataType = Double.class)
    private double price;
    @Mappable(columnNumber = 9, dataType = Double.class)
    private double deposit;
    @Mappable(columnNumber = 10, dataType = Boolean.class)
    private boolean status;
}