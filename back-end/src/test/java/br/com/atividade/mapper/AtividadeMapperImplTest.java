package br.com.atividade.mapper;

import br.com.atividade.model.Atividade;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do AtividadeMapperImpl")
class AtividadeMapperImplTest {

    private AtividadeMapperImpl mapper;
    private AtividadeInput validInput;
    private Atividade validEntity;
    private AtividadeOutput validOutput;

    @BeforeEach
    void setUp() {
        mapper = new AtividadeMapperImpl();

        // AtividadeInput válido para testes
        validInput = new AtividadeInput();
        validInput.setFuncional("EMP001");
        validInput.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        validInput.setCodigoAtividade("RUN");
        validInput.setDescricaoAtividade("Corrida matinal de 5km");

        // Atividade válida para testes
        validEntity = new Atividade();
        validEntity.setIdAtividade(1L);
        validEntity.setFuncional("EMP001");
        validEntity.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        validEntity.setCodigoAtividade("RUN");
        validEntity.setDescricaoAtividade("Corrida matinal de 5km");

        // AtividadeOutput válido para testes
        validOutput = new AtividadeOutput();
        validOutput.setIdAtividade(1L);
        validOutput.setFuncional("EMP001");
        validOutput.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        validOutput.setCodigoAtividade("RUN");
        validOutput.setDescricaoAtividade("Corrida matinal de 5km");
    }

    @Nested
    @DisplayName("Testes do método toEntity")
    class ToEntityTests {

        @Test
        @DisplayName("Deve converter AtividadeInput válido para Atividade")
        void deveConverterAtividadeInputValidoParaAtividade() {
            Atividade result = mapper.toEntity(validInput);

            assertThat(result).isNotNull();
            assertThat(result.getIdAtividade()).isNull(); // ID deve ser ignorado no mapeamento
            assertThat(result.getFuncional()).isEqualTo(validInput.getFuncional());
            assertThat(result.getDataHora()).isEqualTo(validInput.getDataHora());
            assertThat(result.getCodigoAtividade()).isEqualTo(validInput.getCodigoAtividade());
            assertThat(result.getDescricaoAtividade()).isEqualTo(validInput.getDescricaoAtividade());
        }

        @Test
        @DisplayName("Deve retornar null quando AtividadeInput é null")
        void deveRetornarNullQuandoAtividadeInputEhNull() {
            Atividade result = mapper.toEntity(null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Deve converter AtividadeInput com campos nulos")
        void deveConverterAtividadeInputComCamposNulos() {
            AtividadeInput inputComNulos = new AtividadeInput();
            inputComNulos.setFuncional(null);
            inputComNulos.setDataHora(null);
            inputComNulos.setCodigoAtividade(null);
            inputComNulos.setDescricaoAtividade(null);

            Atividade result = mapper.toEntity(inputComNulos);

            assertThat(result).isNotNull();
            assertThat(result.getFuncional()).isNull();
            assertThat(result.getDataHora()).isNull();
            assertThat(result.getCodigoAtividade()).isNull();
            assertThat(result.getDescricaoAtividade()).isNull();
        }

        @Test
        @DisplayName("Deve converter AtividadeInput com campos parciais")
        void deveConverterAtividadeInputComCamposParciais() {
            AtividadeInput inputParcial = new AtividadeInput();
            inputParcial.setFuncional("EMP999");
            inputParcial.setCodigoAtividade("WALK");

            Atividade result = mapper.toEntity(inputParcial);

            assertThat(result).isNotNull();
            assertThat(result.getFuncional()).isEqualTo("EMP999");
            assertThat(result.getCodigoAtividade()).isEqualTo("WALK");
            assertThat(result.getDataHora()).isNull();
            assertThat(result.getDescricaoAtividade()).isNull();
        }
    }

    @Nested
    @DisplayName("Testes do método toOutput")
    class ToOutputTests {

        @Test
        @DisplayName("Deve converter Atividade válida para AtividadeOutput")
        void deveConverterAtividadeValidaParaAtividadeOutput() {
            AtividadeOutput result = mapper.toOutput(validEntity);

            assertThat(result).isNotNull();
            assertThat(result.getIdAtividade()).isEqualTo(validEntity.getIdAtividade());
            assertThat(result.getFuncional()).isEqualTo(validEntity.getFuncional());
            assertThat(result.getDataHora()).isEqualTo(validEntity.getDataHora());
            assertThat(result.getCodigoAtividade()).isEqualTo(validEntity.getCodigoAtividade());
            assertThat(result.getDescricaoAtividade()).isEqualTo(validEntity.getDescricaoAtividade());
        }

        @Test
        @DisplayName("Deve retornar null quando Atividade é null")
        void deveRetornarNullQuandoAtividadeEhNull() {
            AtividadeOutput result = mapper.toOutput(null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Deve converter Atividade com campos nulos")
        void deveConverterAtividadeComCamposNulos() {
            Atividade entityComNulos = new Atividade();
            entityComNulos.setIdAtividade(5L);
            entityComNulos.setFuncional(null);
            entityComNulos.setDataHora(null);
            entityComNulos.setCodigoAtividade(null);
            entityComNulos.setDescricaoAtividade(null);

            AtividadeOutput result = mapper.toOutput(entityComNulos);

            assertThat(result).isNotNull();
            assertThat(result.getIdAtividade()).isEqualTo(5L);
            assertThat(result.getFuncional()).isNull();
            assertThat(result.getDataHora()).isNull();
            assertThat(result.getCodigoAtividade()).isNull();
            assertThat(result.getDescricaoAtividade()).isNull();
        }

        @Test
        @DisplayName("Deve converter Atividade com ID zero")
        void deveConverterAtividadeComIdZero() {
            Atividade entityComIdZero = new Atividade();
            entityComIdZero.setIdAtividade(0L);
            entityComIdZero.setFuncional("EMP000");
            entityComIdZero.setCodigoAtividade("TEST");

            AtividadeOutput result = mapper.toOutput(entityComIdZero);

            assertThat(result).isNotNull();
            assertThat(result.getIdAtividade()).isEqualTo(0L);
            assertThat(result.getFuncional()).isEqualTo("EMP000");
            assertThat(result.getCodigoAtividade()).isEqualTo("TEST");
        }
    }

    @Nested
    @DisplayName("Testes do método toOutputList")
    class ToOutputListTests {

        @Test
        @DisplayName("Deve converter lista vazia para lista vazia")
        void deveConverterListaVaziaParaListaVazia() {
            List<Atividade> listaVazia = new ArrayList<>();

            List<AtividadeOutput> result = mapper.toOutputList(listaVazia);

            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Deve retornar null quando lista é null")
        void deveRetornarNullQuandoListaEhNull() {
            List<AtividadeOutput> result = mapper.toOutputList(null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Deve converter lista com uma atividade")
        void deveConverterListaComUmaAtividade() {
            List<Atividade> listaComUm = Arrays.asList(validEntity);

            List<AtividadeOutput> result = mapper.toOutputList(listaComUm);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getIdAtividade()).isEqualTo(validEntity.getIdAtividade());
            assertThat(result.get(0).getFuncional()).isEqualTo(validEntity.getFuncional());
        }

        @Test
        @DisplayName("Deve converter lista com múltiplas atividades")
        void deveConverterListaComMultiplasAtividades() {
            Atividade atividade2 = new Atividade();
            atividade2.setIdAtividade(2L);
            atividade2.setFuncional("EMP002");
            atividade2.setDataHora(LocalDateTime.of(2025, 12, 26, 15, 45));
            atividade2.setCodigoAtividade("SWIM");
            atividade2.setDescricaoAtividade("Natação 1km");

            Atividade atividade3 = new Atividade();
            atividade3.setIdAtividade(3L);
            atividade3.setFuncional("EMP003");
            atividade3.setDataHora(LocalDateTime.of(2025, 12, 27, 8, 0));
            atividade3.setCodigoAtividade("BIKE");
            atividade3.setDescricaoAtividade("Ciclismo 10km");

            List<Atividade> listaMultiplas = Arrays.asList(validEntity, atividade2, atividade3);

            List<AtividadeOutput> result = mapper.toOutputList(listaMultiplas);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(3);

            // Verificar primeira atividade
            assertThat(result.get(0).getIdAtividade()).isEqualTo(1L);
            assertThat(result.get(0).getFuncional()).isEqualTo("EMP001");
            assertThat(result.get(0).getCodigoAtividade()).isEqualTo("RUN");

            // Verificar segunda atividade
            assertThat(result.get(1).getIdAtividade()).isEqualTo(2L);
            assertThat(result.get(1).getFuncional()).isEqualTo("EMP002");
            assertThat(result.get(1).getCodigoAtividade()).isEqualTo("SWIM");

            // Verificar terceira atividade
            assertThat(result.get(2).getIdAtividade()).isEqualTo(3L);
            assertThat(result.get(2).getFuncional()).isEqualTo("EMP003");
            assertThat(result.get(2).getCodigoAtividade()).isEqualTo("BIKE");
        }

        @Test
        @DisplayName("Deve converter lista contendo elementos nulos")
        void deveConverterListaContendoElementosNulos() {
            List<Atividade> listaComNulos = Arrays.asList(validEntity, null);

            List<AtividadeOutput> result = mapper.toOutputList(listaComNulos);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0)).isNotNull();
            assertThat(result.get(1)).isNull();
        }
    }

    @Nested
    @DisplayName("Testes do método updateEntityFromInput")
    class UpdateEntityFromInputTests {

        @Test
        @DisplayName("Deve atualizar entidade existente com dados do input")
        void deveAtualizarEntidadeExistenteComDadosDoInput() {
            Atividade atividadeExistente = new Atividade();
            atividadeExistente.setIdAtividade(10L);
            atividadeExistente.setFuncional("EMP_ANTIGO");
            atividadeExistente.setDataHora(LocalDateTime.of(2025, 1, 1, 0, 0));
            atividadeExistente.setCodigoAtividade("OLD");
            atividadeExistente.setDescricaoAtividade("Descrição antiga");

            AtividadeInput inputAtualizado = new AtividadeInput();
            inputAtualizado.setFuncional("EMP_NOVO");
            inputAtualizado.setDataHora(LocalDateTime.of(2025, 12, 31, 23, 59));
            inputAtualizado.setCodigoAtividade("NEW");
            inputAtualizado.setDescricaoAtividade("Nova descrição");

            mapper.updateEntityFromInput(inputAtualizado, atividadeExistente);

            // ID deve permanecer inalterado
            assertThat(atividadeExistente.getIdAtividade()).isEqualTo(10L);
            
            // Outros campos devem ser atualizados
            assertThat(atividadeExistente.getFuncional()).isEqualTo("EMP_NOVO");
            assertThat(atividadeExistente.getDataHora()).isEqualTo(LocalDateTime.of(2025, 12, 31, 23, 59));
            assertThat(atividadeExistente.getCodigoAtividade()).isEqualTo("NEW");
            assertThat(atividadeExistente.getDescricaoAtividade()).isEqualTo("Nova descrição");
        }

        @Test
        @DisplayName("Não deve alterar entidade quando input é null")
        void naoDeveAlterarEntidadeQuandoInputEhNull() {
            Atividade atividadeOriginal = new Atividade();
            atividadeOriginal.setIdAtividade(15L);
            atividadeOriginal.setFuncional("EMP_ORIGINAL");
            atividadeOriginal.setDataHora(LocalDateTime.of(2025, 6, 15, 12, 0));
            atividadeOriginal.setCodigoAtividade("ORIGINAL");
            atividadeOriginal.setDescricaoAtividade("Descrição original");

            // Criar cópia para comparação
            Atividade copiaOriginal = new Atividade();
            copiaOriginal.setIdAtividade(atividadeOriginal.getIdAtividade());
            copiaOriginal.setFuncional(atividadeOriginal.getFuncional());
            copiaOriginal.setDataHora(atividadeOriginal.getDataHora());
            copiaOriginal.setCodigoAtividade(atividadeOriginal.getCodigoAtividade());
            copiaOriginal.setDescricaoAtividade(atividadeOriginal.getDescricaoAtividade());

            mapper.updateEntityFromInput(null, atividadeOriginal);

            // Todos os campos devem permanecer inalterados
            assertThat(atividadeOriginal.getIdAtividade()).isEqualTo(copiaOriginal.getIdAtividade());
            assertThat(atividadeOriginal.getFuncional()).isEqualTo(copiaOriginal.getFuncional());
            assertThat(atividadeOriginal.getDataHora()).isEqualTo(copiaOriginal.getDataHora());
            assertThat(atividadeOriginal.getCodigoAtividade()).isEqualTo(copiaOriginal.getCodigoAtividade());
            assertThat(atividadeOriginal.getDescricaoAtividade()).isEqualTo(copiaOriginal.getDescricaoAtividade());
        }

        @Test
        @DisplayName("Deve atualizar com valores nulos do input")
        void deveAtualizarComValoresNulosDoInput() {
            Atividade atividadeExistente = new Atividade();
            atividadeExistente.setIdAtividade(20L);
            atividadeExistente.setFuncional("EMP_EXISTENTE");
            atividadeExistente.setDataHora(LocalDateTime.of(2025, 3, 15, 9, 30));
            atividadeExistente.setCodigoAtividade("EXIST");
            atividadeExistente.setDescricaoAtividade("Descrição existente");

            AtividadeInput inputComNulos = new AtividadeInput();
            inputComNulos.setFuncional(null);
            inputComNulos.setDataHora(null);
            inputComNulos.setCodigoAtividade(null);
            inputComNulos.setDescricaoAtividade(null);

            mapper.updateEntityFromInput(inputComNulos, atividadeExistente);

            // ID deve permanecer inalterado
            assertThat(atividadeExistente.getIdAtividade()).isEqualTo(20L);
            
            // Outros campos devem ser atualizados para null
            assertThat(atividadeExistente.getFuncional()).isNull();
            assertThat(atividadeExistente.getDataHora()).isNull();
            assertThat(atividadeExistente.getCodigoAtividade()).isNull();
            assertThat(atividadeExistente.getDescricaoAtividade()).isNull();
        }

        @Test
        @DisplayName("Deve atualizar parcialmente com alguns campos nulos")
        void deveAtualizarParcialmenteComAlgunsCamposNulos() {
            Atividade atividadeExistente = new Atividade();
            atividadeExistente.setIdAtividade(25L);
            atividadeExistente.setFuncional("EMP_ANTIGO");
            atividadeExistente.setDataHora(LocalDateTime.of(2025, 1, 1, 8, 0));
            atividadeExistente.setCodigoAtividade("OLD_CODE");
            atividadeExistente.setDescricaoAtividade("Descrição antiga");

            AtividadeInput inputParcial = new AtividadeInput();
            inputParcial.setFuncional("EMP_NOVO");
            inputParcial.setDataHora(null);
            inputParcial.setCodigoAtividade("NEW_CODE");
            inputParcial.setDescricaoAtividade(null);

            mapper.updateEntityFromInput(inputParcial, atividadeExistente);

            // ID deve permanecer inalterado
            assertThat(atividadeExistente.getIdAtividade()).isEqualTo(25L);
            
            // Campos atualizados
            assertThat(atividadeExistente.getFuncional()).isEqualTo("EMP_NOVO");
            assertThat(atividadeExistente.getCodigoAtividade()).isEqualTo("NEW_CODE");
            
            // Campos definidos como null
            assertThat(atividadeExistente.getDataHora()).isNull();
            assertThat(atividadeExistente.getDescricaoAtividade()).isNull();
        }
    }

    @Nested
    @DisplayName("Testes de Integração e Casos Especiais")
    class IntegracaoECasosEspeciaisTests {

        @Test
        @DisplayName("Deve manter consistência ao converter ida e volta")
        void deveManterConsistenciaAoConverterIdaEVolta() {
            // Input → Entity → Output
            Atividade entity = mapper.toEntity(validInput);
            entity.setIdAtividade(100L); // Simular ID gerado pelo banco
            AtividadeOutput output = mapper.toOutput(entity);

            // Verificar consistência
            assertThat(output.getFuncional()).isEqualTo(validInput.getFuncional());
            assertThat(output.getDataHora()).isEqualTo(validInput.getDataHora());
            assertThat(output.getCodigoAtividade()).isEqualTo(validInput.getCodigoAtividade());
            assertThat(output.getDescricaoAtividade()).isEqualTo(validInput.getDescricaoAtividade());
            assertThat(output.getIdAtividade()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Deve processar corretamente strings com caracteres especiais")
        void deveProcessarCorretamenteStringsComCaracteresEspeciais() {
            AtividadeInput inputEspecial = new AtividadeInput();
            inputEspecial.setFuncional("EMP-001_TESTE");
            inputEspecial.setCodigoAtividade("RUN@HOME#2025");
            inputEspecial.setDescricaoAtividade("Corrida com acentuação: São Paulo, 5km às 10h30min");
            inputEspecial.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));

            Atividade entity = mapper.toEntity(inputEspecial);

            assertThat(entity.getFuncional()).isEqualTo("EMP-001_TESTE");
            assertThat(entity.getCodigoAtividade()).isEqualTo("RUN@HOME#2025");
            assertThat(entity.getDescricaoAtividade()).isEqualTo("Corrida com acentuação: São Paulo, 5km às 10h30min");
        }

        @Test
        @DisplayName("Deve processar datas limite corretamente")
        void deveProcessarDatasLimiteCorretamente() {
            LocalDateTime dataMinima = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            LocalDateTime dataMaxima = LocalDateTime.of(2099, 12, 31, 23, 59, 59);

            AtividadeInput inputDataMinima = new AtividadeInput();
            inputDataMinima.setDataHora(dataMinima);
            inputDataMinima.setFuncional("EMP001");
            inputDataMinima.setCodigoAtividade("MIN");

            AtividadeInput inputDataMaxima = new AtividadeInput();
            inputDataMaxima.setDataHora(dataMaxima);
            inputDataMaxima.setFuncional("EMP002");
            inputDataMaxima.setCodigoAtividade("MAX");

            Atividade entityMinima = mapper.toEntity(inputDataMinima);
            Atividade entityMaxima = mapper.toEntity(inputDataMaxima);

            assertThat(entityMinima.getDataHora()).isEqualTo(dataMinima);
            assertThat(entityMaxima.getDataHora()).isEqualTo(dataMaxima);
        }
    }
}