package models.people;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Mappable;

import java.sql.Date;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Mappable(columnNumber = 1, dataType = UUID.class)
    private UUID id;
    @Mappable(columnNumber = 2, dataType = String.class)
    private String name;
    @Mappable(columnNumber = 3, dataType = String.class)
    private String surname;
    @Mappable(columnNumber = 4, dataType = String.class)
    private String patronymic;
    @Mappable(columnNumber = 5, dataType = Date.class)
    private Date dateOfBirth;
    @Mappable(columnNumber = 6, dataType = String.class)
    private String email;
    @Mappable(columnNumber = 7, dataType = String.class)
    private String password;
    @Mappable(columnNumber = 8, dataType = UUID.class)
    private UUID roleId;
    @Mappable(columnNumber = 9, dataType = Boolean.class)
    private boolean status;
}