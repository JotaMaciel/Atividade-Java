package com.exemplo.academia.controller;

import com.exemplo.academia.dto.AlunoRequest;
import com.exemplo.academia.dto.AlunoResponse;
import com.exemplo.academia.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponse> matricular(@Valid @RequestBody AlunoRequest request) {
        AlunoResponse criado = alunoService.matricular(request);
        URI uri = URI.create("/api/alunos/" + criado.id());
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listar(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(alunoService.listar(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
        return ResponseEntity.ok(alunoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AlunoResponse> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(alunoService.alterarStatus(id, ativo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
