package com.todolist;

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
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = repository.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
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
        List<Tarefa> tarefas = repository.findByConcluida(concluida);
        return ResponseEntity.ok(tarefas);
    }

    // UPDATE - Atualizar tarefa
    // PUT http://localhost:8080/api/tarefas/1
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        Optional<Tarefa> tarefaExistente = repository.findById(id);

        if (tarefaExistente.isPresent()) {
            Tarefa tarefa = tarefaExistente.get();
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setConcluida(tarefaAtualizada.getConcluida());

            Tarefa tarefaSalva = repository.save(tarefa);
            return ResponseEntity.ok(tarefaSalva);
        } else {
            return ResponseEntity.notFound().build();
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

/* @RestController  // ← Esta classe é uma API REST

@RequestMapping("/api/tarefas")  // ← Todas as rotas começam com /api/tarefas

@Autowired  // ← Spring injeta o repository automaticamente
```

### **CRUD completo:**

| Método HTTP | URL | O que faz |
|------------|-----|-----------|
| **POST** | `/api/tarefas` | ✅ Criar tarefa |
| **GET** | `/api/tarefas` | 📋 Listar todas |
| **GET** | `/api/tarefas/1` | 🔍 Buscar por ID |
| **GET** | `/api/tarefas/status/false` | 📋 Listar pendentes |
| **PUT** | `/api/tarefas/1` | ✏️ Atualizar tarefa |
| **PATCH** | `/api/tarefas/1/concluir` | ☑️ Marcar concluída |
| **DELETE** | `/api/tarefas/1` | 🗑️ Deletar tarefa |
| **DELETE** | `/api/tarefas` | 🗑️ Deletar todas |

---

## ✅ **Salve o arquivo:**

`Ctrl + S` ou `Cmd + S`

---

## 🔄 **IMPORTANTE: Reiniciar a aplicação**

Como você criou novos arquivos, precisa **reiniciar** o Spring Boot:

1. **No console do IntelliJ** (parte de baixo), procura o botão **🛑 Stop**

2. **Clica nele** para parar a aplicação

3. **Roda novamente:** Clica no **▶️ Run** ou aperta `Shift + F10`

---

## 🎉 **Aguarde iniciar e...**

Quando aparecer no console:
```
Started TodolistApplication in X seconds
```

**Sua API está PRONTA!** 🚀

---

## 🧪 **Vamos testar agora!**

Vou te ensinar a testar cada endpoint.

---

## 📝 **Me avisa:**
```
[ ] Criei TarefaController
[ ] Colei o código
[ ] Salvei
[ ] Reiniciei a aplicação
[ ] Aplicação rodando novamente
[ ] Pronta para testar!
[ ] Deu erro: _______________
*/

