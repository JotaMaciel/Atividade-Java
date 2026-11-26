package com.exemplo.academia.service;

import com.exemplo.academia.dto.PlanoRequest;
import com.exemplo.academia.dto.PlanoResponse;
import com.exemplo.academia.exception.RecursoNaoEncontradoException;
import com.exemplo.academia.exception.RegraNegocioException;
import com.exemplo.academia.model.Plano;
import com.exemplo.academia.repository.AlunoRepository;
import com.exemplo.academia.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final AlunoRepository alunoRepository;

    @Transactional
    public PlanoResponse cadastrar(PlanoRequest request) {
        if (planoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException("Já existe um plano cadastrado com o nome: " + request.nome());
        }
        Plano plano = new Plano();
        plano.setNome(request.nome());
        plano.setValorMensal(request.valorMensal());
        plano.setDuracaoMeses(request.duracaoMeses());

        return PlanoResponse.fromEntity(planoRepository.save(plano));
    }

    @Transactional(readOnly = true)
    public List<PlanoResponse> listar() {
        return planoRepository.findAll().stream()
                .map(PlanoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanoResponse buscarPorId(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));
        return PlanoResponse.fromEntity(plano);
    }

    @Transactional
    public void excluir(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id);
        }
        if (alunoRepository.existsByPlanoId(id)) {
            throw new RegraNegocioException("Não é possível excluir um plano que possui alunos vinculados");
        }
        planoRepository.deleteById(id);
    }
}
