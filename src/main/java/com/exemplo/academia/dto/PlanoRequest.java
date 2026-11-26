package com.exemplo.academia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlanoRequest(
        @NotBlank(message = "O nome do plano é obrigatório")
        String nome,

        @NotNull(message = "O valor mensal é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor mensal deve ser maior que zero")
        BigDecimal valorMensal,

        @NotNull(message = "A duração em meses é obrigatória")
        @Min(value = 1, message = "A duração mínima é de 1 mês")
        Integer duracaoMeses
) {}
