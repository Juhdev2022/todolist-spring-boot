package com.todolist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

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

    // Spring entende automaticamente e cria a query SQL!
    // SELECT * FROM tarefas WHERE concluida = ?
}