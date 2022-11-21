package services.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PasswordSize {
    MAX_SIZE(20),
    MIN_SIZE(8);

    private final int value;
}