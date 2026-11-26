package com.exemplo.academia.dto;

import com.exemplo.academia.model.Aluno;
import java.math.BigDecimal;

public record AlunoResponse(
        Long id,
        String nome,
        String email,
        String cpf,
        Boolean ativo,
        Long planoId,
        String nomePlano,
        BigDecimal valorTotalContrato
) {
    public static AlunoResponse fromEntity(Aluno aluno) {
        BigDecimal total = aluno.getPlano().getValorMensal()
                .multiply(BigDecimal.valueOf(aluno.getPlano().getDuracaoMeses()));

        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getCpf(),
                aluno.getAtivo(),
                aluno.getPlano().getId(),
                aluno.getPlano().getNome(),
                total
        );
    }
}
