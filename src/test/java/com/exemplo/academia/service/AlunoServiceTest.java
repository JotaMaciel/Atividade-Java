package com.exemplo.academia.service;

import com.exemplo.academia.dto.AlunoRequest;
import com.exemplo.academia.dto.AlunoResponse;
import com.exemplo.academia.exception.RecursoNaoEncontradoException;
import com.exemplo.academia.exception.RegraNegocioException;
import com.exemplo.academia.model.Aluno;
import com.exemplo.academia.model.Plano;
import com.exemplo.academia.repository.AlunoRepository;
import com.exemplo.academia.repository.PlanoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PlanoRepository planoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private Plano planoTrimestral;
    private AlunoRequest requestValido;

    @BeforeEach
    void setUp() {
        planoTrimestral = new Plano();
        planoTrimestral.setId(1L);
        planoTrimestral.setNome("Plano Trimestral");
        planoTrimestral.setValorMensal(new BigDecimal("120.00"));
        planoTrimestral.setDuracaoMeses(3);

        requestValido = new AlunoRequest("Lucas Silva", "lucas@email.com", "12345678901", 1L);
    }

    @Test
    @DisplayName("Deve matricular aluno com sucesso e calcular valor total do contrato")
    void deveMatricularAlunoComSucesso() {
        when(alunoRepository.existsByCpf(requestValido.cpf())).thenReturn(false);
        when(alunoRepository.existsByEmailIgnoreCase(requestValido.email())).thenReturn(false);
        when(planoRepository.findById(1L)).thenReturn(Optional.of(planoTrimestral));
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno salvo = invocation.getArgument(0);
            salvo.setId(10L);
            return salvo;
        });

        AlunoResponse response = alunoService.matricular(requestValido);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Lucas Silva", response.nome());
        assertEquals("Plano Trimestral", response.nomePlano());
        assertEquals(new BigDecimal("360.00"), response.valorTotalContrato());
        assertTrue(response.ativo());
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já estiver cadastrado")
    void deveLancarExcecaoQuandoCpfDuplicado() {
        when(alunoRepository.existsByCpf(requestValido.cpf())).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> alunoService.matricular(requestValido));
        verify(alunoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando plano informado não existir")
    void deveLancarExcecaoQuandoPlanoInexistente() {
        when(alunoRepository.existsByCpf(requestValido.cpf())).thenReturn(false);
        when(alunoRepository.existsByEmailIgnoreCase(requestValido.email())).thenReturn(false);
        when(planoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.matricular(requestValido));
        verify(alunoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar aluno com ID inexistente")
    void deveLancarExcecaoAoBuscarAlunoInexistente() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> alunoService.buscarPorId(99L));
    }
}
