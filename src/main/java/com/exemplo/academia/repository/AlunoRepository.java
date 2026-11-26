package com.exemplo.academia.repository;

import com.exemplo.academia.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPlanoId(Long planoId);
    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    @Query("select a from Aluno a join fetch a.plano")
    List<Aluno> buscarTodosComPlano();
}
