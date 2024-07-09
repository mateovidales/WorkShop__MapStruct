package com.riwi.LibrosYa.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotNull(message = "El estatus es requerido")
    private boolean status;

    @NotNull(message = "EL id del usuario es requerido")
    @Positive(message = "El id del usuario debe ser un número positivo")
    private Long userId;

    
    @NotNull(message = "EL id del libro es requerido")
    @Positive(message = "El id del libro debe ser un número positivo")
    private Long bookId;
}
