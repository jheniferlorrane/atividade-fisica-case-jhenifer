package br.com.atividade.service.dto.input;

import br.com.atividade.sevice.dto.input.AtividadeInput;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Testes do AtividadeInput")
public class AtividadeInputTest {

    private Validator validator;
    private AtividadeInput atividadeInput;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        atividadeInput = new AtividadeInput();
        atividadeInput.setFuncional("EMP001");
        atividadeInput.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividadeInput.setCodigoAtividade("RUN");
        atividadeInput.setDescricaoAtividade("Corrida matinal de 5km");
    }

    @Test
    @DisplayName("Deve validar AtividadeInput com dados válidos")
    void deveValidarAtividadeInputComDadosValidos() {
        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar validação quando funcional é nulo")
    void deveFalharValidacaoQuandoFuncionalEhNulo() {
        atividadeInput.setFuncional(null);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Funcional não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar validação quando funcional é vazio")
    void deveFalharValidacaoQuandoFuncionalEhVazio() {
        atividadeInput.setFuncional("");

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Funcional não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar validação quando funcional excede 50 caracteres")
    void deveFalharValidacaoQuandoFuncionalExcede50Caracteres() {
        String funcionalLongo = "A".repeat(51); // 51 caracteres
        atividadeInput.setFuncional(funcionalLongo);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Funcional não pode ter mais de 50 caracteres");
    }

    @Test
    @DisplayName("Deve falhar validação quando dataHora é nula")
    void deveFalharValidacaoQuandoDataHoraEhNula() {
        atividadeInput.setDataHora(null);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Data e hora da atividade não podem estar vazias");
    }

    @Test
    @DisplayName("Deve falhar validação quando codigoAtividade é nulo")
    void deveFalharValidacaoQuandoCodigoAtividadeEhNulo() {
        atividadeInput.setCodigoAtividade(null);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Código da atividade não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar validação quando codigoAtividade é vazio")
    void deveFalharValidacaoQuandoCodigoAtividadeEhVazio() {
        atividadeInput.setCodigoAtividade("");

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Código da atividade não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar validação quando codigoAtividade excede 20 caracteres")
    void deveFalharValidacaoQuandoCodigoAtividadeExcede20Caracteres() {
        String codigoLongo = "A".repeat(21); // 21 caracteres
        atividadeInput.setCodigoAtividade(codigoLongo);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Código da atividade não pode ter mais de 20 caracteres");
    }

    @Test
    @DisplayName("Deve falhar validação quando descricaoAtividade é nula")
    void deveFalharValidacaoQuandoDescricaoAtividadeEhNula() {
        atividadeInput.setDescricaoAtividade(null);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Descrição da atividade não pode estar vazia");
    }

    @Test
    @DisplayName("Deve falhar validação quando descricaoAtividade é vazia")
    void deveFalharValidacaoQuandoDescricaoAtividadeEhVazia() {
        atividadeInput.setDescricaoAtividade("");

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Descrição da atividade não pode estar vazia");
    }

    @Test
    @DisplayName("Deve falhar validação quando descricaoAtividade excede 255 caracteres")
    void deveFalharValidacaoQuandoDescricaoAtividadeExcede255Caracteres() {
        String descricaoLonga = "A".repeat(256); // 256 caracteres
        atividadeInput.setDescricaoAtividade(descricaoLonga);

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Descrição da atividade não pode ter mais de 255 caracteres");
    }

    @Test
    @DisplayName("Deve aceitar valores no limite máximo dos campos")
    void deveAceitarValoresNoLimiteMaximoDosCampos() {
        atividadeInput.setFuncional("A".repeat(50)); // Exatamente 50 caracteres
        atividadeInput.setCodigoAtividade("B".repeat(20)); // Exatamente 20 caracteres
        atividadeInput.setDescricaoAtividade("C".repeat(255)); // Exatamente 255 caracteres

        Set<ConstraintViolation<AtividadeInput>> violations = validator.validate(atividadeInput);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve ser igual quando todos os campos são iguais")
    void deveSerIgualQuandoTodosCamposSaoIguais() {
        AtividadeInput input1 = criarAtividadeInputCompleta();
        AtividadeInput input2 = criarAtividadeInputCompleta();

        assertThat(input1).isEqualTo(input2);
        assertThat(input1.hashCode()).isEqualTo(input2.hashCode());
    }

    @Test
    @DisplayName("Deve ser diferente quando campos são diferentes")
    void deveSerDiferenteQuandoCamposSaoDiferentes() {
        AtividadeInput input1 = criarAtividadeInputCompleta();
        AtividadeInput input2 = criarAtividadeInputCompleta();
        input2.setFuncional("EMP002"); // Diferente

        assertThat(input1).isNotEqualTo(input2);
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        String toString = atividadeInput.toString();

        assertThat(toString).contains("funcional=EMP001");
        assertThat(toString).contains("codigoAtividade=RUN");
        assertThat(toString).contains("descricaoAtividade=Corrida matinal de 5km");
    }

    private AtividadeInput criarAtividadeInputCompleta() {
        AtividadeInput input = new AtividadeInput();
        input.setFuncional("EMP001");
        input.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        input.setCodigoAtividade("RUN");
        input.setDescricaoAtividade("Corrida matinal de 5km");
        return input;
    }
}
