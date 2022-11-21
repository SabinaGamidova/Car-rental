package models.session;

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
public class Session {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;

    @Mappable(columnNumber = 2, dataType = UUID.class)
    private UUID userId;

    @Mappable(columnNumber = 3, dataType = Date.class)
    private Date startTime;

    @Mappable(columnNumber = 4, dataType = Date.class)
    private Date finishTime;

    @Mappable(columnNumber = 5, dataType = Boolean.class)
    private boolean status;
}