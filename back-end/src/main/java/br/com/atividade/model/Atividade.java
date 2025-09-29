package br.com.atividade.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long idAtividade;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao_atividade")
    private String descricaoAtividade;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "funcional")
    private String funcional;
}
