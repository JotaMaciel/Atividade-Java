package com.exemplo.academia.service;

import com.exemplo.academia.dto.AlunoRequest;
import com.exemplo.academia.dto.AlunoResponse;
import com.exemplo.academia.exception.RecursoNaoEncontradoException;
import com.exemplo.academia.exception.RegraNegocioException;
import com.exemplo.academia.model.Aluno;
import com.exemplo.academia.model.Plano;
import com.exemplo.academia.repository.AlunoRepository;
import com.exemplo.academia.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;

    @Transactional
    public AlunoResponse matricular(AlunoRequest request) {
        if (alunoRepository.existsByCpf(request.cpf())) {
            throw new RegraNegocioException("Já existe um aluno cadastrado com o CPF informado");
        }
        if (alunoRepository.existsByEmailIgnoreCase(request.email())) {
            throw new RegraNegocioException("Já existe um aluno cadastrado com o e-mail informado");
        }

        Plano plano = planoRepository.findById(request.planoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + request.planoId()));

        Aluno aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        aluno.setCpf(request.cpf());
        aluno.setAtivo(true);
        aluno.setPlano(plano);

        return AlunoResponse.fromEntity(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listar(String nome) {
        List<Aluno> alunos = (nome != null && !nome.isBlank())
                ? alunoRepository.findByNomeContainingIgnoreCase(nome)
                : alunoRepository.buscarTodosComPlano();

        return alunos.stream()
                .map(AlunoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
        return AlunoResponse.fromEntity(aluno);
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));

        Plano plano = planoRepository.findById(request.planoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + request.planoId()));

        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        aluno.setCpf(request.cpf());
        aluno.setPlano(plano);

        return AlunoResponse.fromEntity(alunoRepository.save(aluno));
    }

    @Transactional
    public AlunoResponse alterarStatus(Long id, boolean ativo) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
        aluno.setAtivo(ativo);
        return AlunoResponse.fromEntity(alunoRepository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}
