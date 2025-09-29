package br.com.atividade.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da entidade Atividade")
class AtividadeTest {

    private Atividade atividade;

    @BeforeEach
    void setUp() {
        atividade = new Atividade();
    }

    @Test
    @DisplayName("Deve criar atividade com todos os campos preenchidos")
    void deveCriarAtividadeComTodosCamposPreenchidos() {
        // Given
        Long id = 1L;
        String funcional = "EMP001";
        LocalDateTime dataHora = LocalDateTime.of(2025, 12, 25, 10, 30);
        String codigoAtividade = "RUN";
        String descricaoAtividade = "Corrida matinal de 5km";

        // When
        atividade.setIdAtividade(id);
        atividade.setFuncional(funcional);
        atividade.setDataHora(dataHora);
        atividade.setCodigoAtividade(codigoAtividade);
        atividade.setDescricaoAtividade(descricaoAtividade);

        // Then
        assertThat(atividade.getIdAtividade()).isEqualTo(id);
        assertThat(atividade.getFuncional()).isEqualTo(funcional);
        assertThat(atividade.getDataHora()).isEqualTo(dataHora);
        assertThat(atividade.getCodigoAtividade()).isEqualTo(codigoAtividade);
        assertThat(atividade.getDescricaoAtividade()).isEqualTo(descricaoAtividade);
    }

    @Test
    @DisplayName("Deve aceitar valores nulos nos campos")
    void deveAceitarValoresNulosNosCampos() {
        // When
        atividade.setIdAtividade(null);
        atividade.setFuncional(null);
        atividade.setDataHora(null);
        atividade.setCodigoAtividade(null);
        atividade.setDescricaoAtividade(null);

        // Then
        assertThat(atividade.getIdAtividade()).isNull();
        assertThat(atividade.getFuncional()).isNull();
        assertThat(atividade.getDataHora()).isNull();
        assertThat(atividade.getCodigoAtividade()).isNull();
        assertThat(atividade.getDescricaoAtividade()).isNull();
    }

    @Test
    @DisplayName("Deve ser igual quando todos os campos são iguais")
    void deveSerIgualQuandoTodosCamposSaoIguais() {
        // Given
        Atividade atividade1 = criarAtividadeCompleta();
        Atividade atividade2 = criarAtividadeCompleta();

        // When & Then
        assertThat(atividade1).isEqualTo(atividade2);
        assertThat(atividade1.hashCode()).isEqualTo(atividade2.hashCode());
    }

    @Test
    @DisplayName("Deve ser diferente quando campos são diferentes")
    void deveSerDiferenteQuandoCamposSaoDiferentes() {
        // Given
        Atividade atividade1 = criarAtividadeCompleta();
        Atividade atividade2 = criarAtividadeCompleta();
        atividade2.setFuncional("EMP002"); // Diferente

        // When & Then
        assertThat(atividade1).isNotEqualTo(atividade2);
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        // Given
        atividade = criarAtividadeCompleta();

        // When
        String toString = atividade.toString();

        // Then
        assertThat(toString).contains("idAtividade=1");
        assertThat(toString).contains("funcional=EMP001");
        assertThat(toString).contains("codigoAtividade=RUN");
        assertThat(toString).contains("descricaoAtividade=Corrida matinal de 5km");
    }

    @Test
    @DisplayName("Deve permitir modificação dos campos após criação")
    void devePermitirModificacaoDosCApos() {
        // Given
        atividade = criarAtividadeCompleta();
        String novoFuncional = "EMP002";
        String novoCodigoAtividade = "WALK";

        // When
        atividade.setFuncional(novoFuncional);
        atividade.setCodigoAtividade(novoCodigoAtividade);

        // Then
        assertThat(atividade.getFuncional()).isEqualTo(novoFuncional);
        assertThat(atividade.getCodigoAtividade()).isEqualTo(novoCodigoAtividade);
    }

    @Test
    @DisplayName("Deve aceitar strings vazias nos campos de texto")
    void deveAceitarStringsVaziasNosCamposDeTexto() {
        // When
        atividade.setFuncional("");
        atividade.setCodigoAtividade("");
        atividade.setDescricaoAtividade("");

        // Then
        assertThat(atividade.getFuncional()).isEmpty();
        assertThat(atividade.getCodigoAtividade()).isEmpty();
        assertThat(atividade.getDescricaoAtividade()).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar data/hora específica")
    void deveAceitarDataHoraEspecifica() {
        // Given
        LocalDateTime dataEspecifica = LocalDateTime.of(2024, 1, 15, 14, 45, 30);

        // When
        atividade.setDataHora(dataEspecifica);

        // Then
        assertThat(atividade.getDataHora()).isEqualTo(dataEspecifica);
        assertThat(atividade.getDataHora().getYear()).isEqualTo(2024);
        assertThat(atividade.getDataHora().getMonthValue()).isEqualTo(1);
        assertThat(atividade.getDataHora().getDayOfMonth()).isEqualTo(15);
        assertThat(atividade.getDataHora().getHour()).isEqualTo(14);
        assertThat(atividade.getDataHora().getMinute()).isEqualTo(45);
        assertThat(atividade.getDataHora().getSecond()).isEqualTo(30);
    }

    private Atividade criarAtividadeCompleta() {
        Atividade atividade = new Atividade();
        atividade.setIdAtividade(1L);
        atividade.setFuncional("EMP001");
        atividade.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividade.setCodigoAtividade("RUN");
        atividade.setDescricaoAtividade("Corrida matinal de 5km");
        return atividade;
    }
}
