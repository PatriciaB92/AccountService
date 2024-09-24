package account;

import jakarta.validation.constraints.NotBlank;

public record UserLoggingRequest(@NotBlank String email, @NotBlank String password) {
}
