package br.com.atividade.service.dto.output;

import br.com.atividade.sevice.dto.output.AtividadeOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do AtividadeOutput")
public class AtividadeOutputTest {

        private AtividadeOutput atividadeOutput;

        @BeforeEach
        void setUp() {
            atividadeOutput = new AtividadeOutput();
        }

        @Test
        @DisplayName("Deve criar AtividadeOutput com todos os campos preenchidos")
        void deveCriarAtividadeOutputComTodosCamposPreenchidos() {
            // Given
            Long id = 1L;
            String funcional = "EMP001";
            LocalDateTime dataHora = LocalDateTime.of(2025, 12, 25, 10, 30);
            String codigoAtividade = "RUN";
            String descricaoAtividade = "Corrida matinal de 5km";

            // When
            atividadeOutput.setIdAtividade(id);
            atividadeOutput.setFuncional(funcional);
            atividadeOutput.setDataHora(dataHora);
            atividadeOutput.setCodigoAtividade(codigoAtividade);
            atividadeOutput.setDescricaoAtividade(descricaoAtividade);

            // Then
            assertThat(atividadeOutput.getIdAtividade()).isEqualTo(id);
            assertThat(atividadeOutput.getFuncional()).isEqualTo(funcional);
            assertThat(atividadeOutput.getDataHora()).isEqualTo(dataHora);
            assertThat(atividadeOutput.getCodigoAtividade()).isEqualTo(codigoAtividade);
            assertThat(atividadeOutput.getDescricaoAtividade()).isEqualTo(descricaoAtividade);
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos campos")
        void deveAceitarValoresNulosNosCampos() {
            // When
            atividadeOutput.setIdAtividade(null);
            atividadeOutput.setFuncional(null);
            atividadeOutput.setDataHora(null);
            atividadeOutput.setCodigoAtividade(null);
            atividadeOutput.setDescricaoAtividade(null);

            // Then
            assertThat(atividadeOutput.getIdAtividade()).isNull();
            assertThat(atividadeOutput.getFuncional()).isNull();
            assertThat(atividadeOutput.getDataHora()).isNull();
            assertThat(atividadeOutput.getCodigoAtividade()).isNull();
            assertThat(atividadeOutput.getDescricaoAtividade()).isNull();
        }

        @Test
        @DisplayName("Deve ser igual quando todos os campos são iguais")
        void deveSerIgualQuandoTodosCamposSaoIguais() {
            // Given
            AtividadeOutput output1 = criarAtividadeOutputCompleta();
            AtividadeOutput output2 = criarAtividadeOutputCompleta();

            // When & Then
            assertThat(output1).isEqualTo(output2);
            assertThat(output1.hashCode()).isEqualTo(output2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando campos são diferentes")
        void deveSerDiferenteQuandoCamposSaoDiferentes() {
            // Given
            AtividadeOutput output1 = criarAtividadeOutputCompleta();
            AtividadeOutput output2 = criarAtividadeOutputCompleta();
            output2.setFuncional("EMP002"); // Diferente

            // When & Then
            assertThat(output1).isNotEqualTo(output2);
        }

        @Test
        @DisplayName("Deve gerar toString corretamente")
        void deveGerarToStringCorretamente() {
            // Given
            atividadeOutput = criarAtividadeOutputCompleta();

            // When
            String toString = atividadeOutput.toString();

            // Then
            assertThat(toString).contains("idAtividade=1");
            assertThat(toString).contains("funcional=EMP001");
            assertThat(toString).contains("codigoAtividade=RUN");
            assertThat(toString).contains("descricaoAtividade=Corrida matinal de 5km");
        }

        @Test
        @DisplayName("Deve permitir modificação dos campos após criação")
        void devePermitirModificacaoDosCamposAposCriacao() {
            // Given
            atividadeOutput = criarAtividadeOutputCompleta();
            String novoFuncional = "EMP002";
            String novoCodigoAtividade = "WALK";
            Long novoId = 2L;

            // When
            atividadeOutput.setIdAtividade(novoId);
            atividadeOutput.setFuncional(novoFuncional);
            atividadeOutput.setCodigoAtividade(novoCodigoAtividade);

            // Then
            assertThat(atividadeOutput.getIdAtividade()).isEqualTo(novoId);
            assertThat(atividadeOutput.getFuncional()).isEqualTo(novoFuncional);
            assertThat(atividadeOutput.getCodigoAtividade()).isEqualTo(novoCodigoAtividade);
        }

        @Test
        @DisplayName("Deve aceitar strings vazias nos campos de texto")
        void deveAceitarStringsVaziasNosCamposDeTexto() {
            // When
            atividadeOutput.setFuncional("");
            atividadeOutput.setCodigoAtividade("");
            atividadeOutput.setDescricaoAtividade("");

            // Then
            assertThat(atividadeOutput.getFuncional()).isEmpty();
            assertThat(atividadeOutput.getCodigoAtividade()).isEmpty();
            assertThat(atividadeOutput.getDescricaoAtividade()).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar ID negativo")
        void deveAceitarIdNegativo() {
            // Given
            Long idNegativo = -1L;

            // When
            atividadeOutput.setIdAtividade(idNegativo);

            // Then
            assertThat(atividadeOutput.getIdAtividade()).isEqualTo(idNegativo);
        }

        @Test
        @DisplayName("Deve aceitar ID zero")
        void deveAceitarIdZero() {
            // Given
            Long idZero = 0L;

            // When
            atividadeOutput.setIdAtividade(idZero);

            // Then
            assertThat(atividadeOutput.getIdAtividade()).isEqualTo(idZero);
        }

        @Test
        @DisplayName("Deve aceitar data/hora específica")
        void deveAceitarDataHoraEspecifica() {
            // Given
            LocalDateTime dataEspecifica = LocalDateTime.of(2024, 1, 15, 14, 45, 30);

            // When
            atividadeOutput.setDataHora(dataEspecifica);

            // Then
            assertThat(atividadeOutput.getDataHora()).isEqualTo(dataEspecifica);
            assertThat(atividadeOutput.getDataHora().getYear()).isEqualTo(2024);
            assertThat(atividadeOutput.getDataHora().getMonthValue()).isEqualTo(1);
            assertThat(atividadeOutput.getDataHora().getDayOfMonth()).isEqualTo(15);
            assertThat(atividadeOutput.getDataHora().getHour()).isEqualTo(14);
            assertThat(atividadeOutput.getDataHora().getMinute()).isEqualTo(45);
            assertThat(atividadeOutput.getDataHora().getSecond()).isEqualTo(30);
        }

        private AtividadeOutput criarAtividadeOutputCompleta() {
            AtividadeOutput output = new AtividadeOutput();
            output.setIdAtividade(1L);
            output.setFuncional("EMP001");
            output.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
            output.setCodigoAtividade("RUN");
            output.setDescricaoAtividade("Corrida matinal de 5km");
            return output;
        }
}
