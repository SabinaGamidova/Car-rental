package models.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Mappable;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;
    @Mappable(columnNumber = 2, dataType = UUID.class)
    private UUID clientId;
    @Mappable(columnNumber = 3, dataType = UUID.class)
    private UUID carId;
    @Mappable(columnNumber = 4, dataType = Date.class)
    private Date from;
    @Mappable(columnNumber = 5, dataType = Date.class)
    private Date to;
    @Mappable(columnNumber = 6, dataType = Double.class)
    private double totalPrice;
    @Mappable(columnNumber = 7, dataType = Boolean.class)
    private boolean status;
}
