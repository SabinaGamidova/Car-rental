package services.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CarMaxPrice {
    PRICE(100_000),
    DEPOSIT(50_000);

    private final int value;
}