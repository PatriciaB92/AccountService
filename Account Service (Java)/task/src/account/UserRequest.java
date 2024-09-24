package account;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String name,@NotBlank String lastname,@NotBlank String email, @NotBlank String password) {
}
