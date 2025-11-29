package com.todolist;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    private String titulo;

    @Column(length = 500)
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Column(nullable = false)
    private Boolean concluida = false;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "hora_inicio")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @Column(name = "hora_termino")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaTermino;

    @Column(name = "tempo_gasto_minutos")
    private Long tempoGastoMinutos;

    // Construtor vazio (obrigatório para JPA)
    public Tarefa() {
        this.dataCriacao = LocalDateTime.now();
    }

    // Construtor com parâmetros
    public Tarefa(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = false;
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
        calcularTempoGasto();
    }

    public LocalTime getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(LocalTime horaTermino) {
        this.horaTermino = horaTermino;
        calcularTempoGasto();
    }

    public Long getTempoGastoMinutos() {
        return tempoGastoMinutos;
    }

    public void setTempoGastoMinutos(Long tempoGastoMinutos) {
        this.tempoGastoMinutos = tempoGastoMinutos;
    }

    // Método para calcular o tempo gasto automaticamente
    private void calcularTempoGasto() {
        if (horaInicio != null && horaTermino != null) {
            if (horaTermino.isAfter(horaInicio) || horaTermino.equals(horaInicio)) {
                Duration duracao = Duration.between(horaInicio, horaTermino);
                this.tempoGastoMinutos = duracao.toMinutes();
            } else {
                // Se a hora de término for anterior à de início, assume que passou meia-noite
                Duration duracao = Duration.between(horaInicio, horaTermino).plusDays(1);
                this.tempoGastoMinutos = duracao.toMinutes();
            }
        } else {
            this.tempoGastoMinutos = null;
        }
    }

    // Método auxiliar para formatar o tempo gasto
    public String getTempoGastoFormatado() {
        if (tempoGastoMinutos == null || tempoGastoMinutos == 0) {
            return "0 min";
        }
        long horas = tempoGastoMinutos / 60;
        long minutos = tempoGastoMinutos % 60;
        if (horas > 0) {
            return String.format("%dh %dm", horas, minutos);
        }
        return String.format("%d min", minutos);
    }
}
