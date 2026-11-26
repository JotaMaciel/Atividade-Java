package com.exemplo.academia.service;

import com.exemplo.academia.exception.RegraNegocioException;
import com.exemplo.academia.repository.AlunoRepository;
import com.exemplo.academia.repository.PlanoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoServiceTest {

    @Mock
    private PlanoRepository planoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private PlanoService planoService;

    @Test
    @DisplayName("Não deve permitir excluir plano que possui alunos vinculados")
    void naoDeveExcluirPlanoComAlunosVinculados() {
        when(planoRepository.existsById(1L)).thenReturn(true);
        when(alunoRepository.existsByPlanoId(1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> planoService.excluir(1L));
        verify(planoRepository, never()).deleteById(any());
    }
}
