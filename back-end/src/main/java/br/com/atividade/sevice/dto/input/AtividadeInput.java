package br.com.atividade.sevice.dto.input;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class AtividadeInput {
    
    @NotBlank(message = "Funcional não pode estar vazio")
    @Size(max = 50, message = "Funcional não pode ter mais de 50 caracteres")
    private String funcional;
    
    @NotNull(message = "Data e hora da atividade não podem estar vazias")
    private LocalDateTime dataHora;
    
    @NotBlank(message = "Código da atividade não pode estar vazio")
    @Size(max = 20, message = "Código da atividade não pode ter mais de 20 caracteres")
    private String codigoAtividade;
    
    @NotBlank(message = "Descrição da atividade não pode estar vazia")
    @Size(max = 255, message = "Descrição da atividade não pode ter mais de 255 caracteres")
    private String descricaoAtividade;
}
