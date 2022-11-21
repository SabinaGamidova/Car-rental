package services.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderMaxPrice {
    MAX_PRICE(200_000);

    private final int value;
}