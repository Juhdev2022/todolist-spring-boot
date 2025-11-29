package com.todolist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Spring cria automaticamente os métodos:
    // save()       → Criar/Atualizar tarefa
    // findAll()    → Buscar todas as tarefas
    // findById()   → Buscar por ID
    // deleteById() → Deletar tarefa
    // count()      → Contar tarefas

    // Você pode adicionar métodos personalizados:
    List<Tarefa> findByConcluida(Boolean concluida);
    List<Tarefa> findByUsuarioId(Long usuarioId);
    List<Tarefa> findByUsuarioIdAndDataTarefa(Long usuarioId, LocalDate dataTarefa);
}
