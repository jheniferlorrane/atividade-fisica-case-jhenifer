package br.com.atividade.sevice.dto.output;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtividadeOutput {
    private Long idAtividade;
    private String funcional;
    private LocalDateTime dataHora;
    private String codigoAtividade;
    private String descricaoAtividade;
}
