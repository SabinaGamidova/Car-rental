package services.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CarMaxPrice {
    MAX_PRICE(100_000),
    MAX_DEPOSIT(50_000);

    private final int value;
}