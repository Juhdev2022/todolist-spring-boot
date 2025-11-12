package com.todolist;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    @Autowired
    private TarefaRepository repository;

    // CREATE - Criar nova tarefa
    // POST http://localhost:8080/api/tarefas
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Tarefa tarefa) {
        try {
            // Garante que a data de criação seja definida
            if (tarefa.getDataCriacao() == null) {
                tarefa.setDataCriacao(java.time.LocalDateTime.now());
            }
            // Garante que concluida seja false por padrão
            if (tarefa.getConcluida() == null) {
                tarefa.setConcluida(false);
            }
            // Processa as horas se fornecidas como string
            if (tarefa.getHoraInicio() == null && tarefa.getHoraTermino() == null) {
                // Se ambas forem null, mantém null
            } else {
                // Se uma hora for fornecida como string, converte para LocalTime
                // Isso será tratado automaticamente pelo Jackson se o formato estiver correto
            }
            Tarefa novaTarefa = repository.save(tarefa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar tarefa: " + e.getMessage());
        }
    }

    // READ - Listar todas as tarefas
    // GET http://localhost:8080/api/tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        List<Tarefa> tarefas = repository.findAll();
        return ResponseEntity.ok(tarefas);
    }

    // READ - Buscar tarefa por ID
    // GET http://localhost:8080/api/tarefas/1
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = repository.findById(id);
        return tarefa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Listar por status (concluídas ou pendentes)
    // GET http://localhost:8080/api/tarefas/status/true
    @GetMapping("/status/{concluida}")
    public ResponseEntity<List<Tarefa>> listarPorStatus(@PathVariable Boolean concluida) {
        try {
            List<Tarefa> tarefas = repository.findByConcluida(concluida);
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Atualizar tarefa
    // PUT http://localhost:8080/api/tarefas/1
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa tarefaAtualizada) {
        try {
            Optional<Tarefa> tarefaExistente = repository.findById(id);

            if (tarefaExistente.isPresent()) {
                Tarefa tarefa = tarefaExistente.get();
                tarefa.setTitulo(tarefaAtualizada.getTitulo());
                tarefa.setDescricao(tarefaAtualizada.getDescricao());
                if (tarefaAtualizada.getConcluida() != null) {
                    tarefa.setConcluida(tarefaAtualizada.getConcluida());
                }
                // Atualiza as horas se fornecidas
                if (tarefaAtualizada.getHoraInicio() != null) {
                    tarefa.setHoraInicio(tarefaAtualizada.getHoraInicio());
                }
                if (tarefaAtualizada.getHoraTermino() != null) {
                    tarefa.setHoraTermino(tarefaAtualizada.getHoraTermino());
                }

                Tarefa tarefaSalva = repository.save(tarefa);
                return ResponseEntity.ok(tarefaSalva);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    // UPDATE - Marcar como concluída/não concluída
    // PATCH http://localhost:8080/api/tarefas/1/concluir
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> alternarConclusao(@PathVariable Long id) {
        Optional<Tarefa> tarefaExistente = repository.findById(id);

        if (tarefaExistente.isPresent()) {
            Tarefa tarefa = tarefaExistente.get();
            tarefa.setConcluida(!tarefa.getConcluida()); // Inverte o status

            Tarefa tarefaSalva = repository.save(tarefa);
            return ResponseEntity.ok(tarefaSalva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Deletar tarefa
    // DELETE http://localhost:8080/api/tarefas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Deletar todas as tarefas
    // DELETE http://localhost:8080/api/tarefas
    @DeleteMapping
    public ResponseEntity<Void> deletarTodas() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}

