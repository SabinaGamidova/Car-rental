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
public class CarComfort {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;
    @Mappable(columnNumber = 2, dataType = String.class)
    private String name;
    @Mappable(columnNumber = 3, dataType = String.class)
    private String description;
    @Mappable(columnNumber = 4, dataType = Boolean.class)
    private boolean status;

    /*PREMIUM(1,"premium"),
    STANDARD(2, "standard"),
    ECONOMY(3, "economy");*/
}