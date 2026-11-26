package com.exemplo.academia.dto;

import com.exemplo.academia.model.Plano;
import java.math.BigDecimal;

public record PlanoResponse(
        Long id,
        String nome,
        BigDecimal valorMensal,
        Integer duracaoMeses
) {
    public static PlanoResponse fromEntity(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getValorMensal(),
                plano.getDuracaoMeses()
        );
    }
}
