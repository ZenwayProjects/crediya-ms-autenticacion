package co.com.zenway.api.dto;

import jakarta.validation.constraints.Email;

public record EmailResponseDTO(
        @Email
        String email
) {

}
