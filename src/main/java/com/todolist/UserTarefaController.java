package com.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/my/tarefas")
public class UserTarefaController {

    @Autowired
    private TarefaRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Tarefa tarefa, HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (tarefa.getDataCriacao() == null) {
            tarefa.setDataCriacao(java.time.LocalDateTime.now());
        }
        if (tarefa.getConcluida() == null) {
            tarefa.setConcluida(false);
        }
        Usuario u = usuarioRepository.findById((Long) usuarioId).orElse(null);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        tarefa.setUsuario(u);
        Tarefa novaTarefa = repository.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar(@RequestParam(required = false) String data, HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (data != null && !data.isBlank()) {
            LocalDate d = LocalDate.parse(data);
            return ResponseEntity.ok(repository.findByUsuarioIdAndDataTarefa((Long) usuarioId, d));
        }
        return ResponseEntity.ok(repository.findByUsuarioId((Long) usuarioId));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> alternarConclusao(@PathVariable Long id, HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Tarefa> tarefaOpt = repository.findById(id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tarefa tarefa = tarefaOpt.get();
        if (tarefa.getUsuario() == null || !tarefa.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        tarefa.setConcluida(!tarefa.getConcluida());
        return ResponseEntity.ok(repository.save(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Tarefa> tarefaOpt = repository.findById(id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tarefa tarefa = tarefaOpt.get();
        if (tarefa.getUsuario() == null || !tarefa.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/soma-tempo")
    public ResponseEntity<Long> somaTempo(@RequestParam(required = false) String data, HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Tarefa> tarefas;
        if (data != null && !data.isBlank()) {
            LocalDate d = LocalDate.parse(data);
            tarefas = repository.findByUsuarioIdAndDataTarefa((Long) usuarioId, d);
        } else {
            tarefas = repository.findByUsuarioId((Long) usuarioId);
        }
        long total = tarefas.stream()
                .filter(t -> t.getTempoGastoMinutos() != null)
                .mapToLong(Tarefa::getTempoGastoMinutos)
                .sum();
        return ResponseEntity.ok(total);
    }
}
