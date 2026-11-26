package com.exemplo.academia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planos")
@Getter
@Setter
@NoArgsConstructor
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorMensal;

    @Column(nullable = false)
    private Integer duracaoMeses;

    @OneToMany(mappedBy = "plano")
    private List<Aluno> alunos = new ArrayList<>();
}
