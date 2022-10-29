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
public class Engine {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;
    @Mappable(columnNumber = 2, dataType = Integer.class)
    private int maxSpeed;
    @Mappable(columnNumber = 3, dataType = UUID.class)
    private UUID fuelTypeId;
    @Mappable(columnNumber = 4, dataType = UUID.class)
    private UUID transmissionTypeId;
    @Mappable(columnNumber = 5, dataType = Double.class)
    private double volume;
    @Mappable(columnNumber = 6, dataType = Double.class)
    private double fuelConsumption;
    @Mappable(columnNumber = 7, dataType = Boolean.class)
    private boolean status;
}
