package com.omnisupport.tickets.infrastructure.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 255)
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 4096)
        String description,

        @NotBlank(message = "El email de contacto es obligatorio")
        @Email(message = "El email no es válido")
        String contactEmail
) {
}
