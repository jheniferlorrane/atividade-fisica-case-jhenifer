package br.com.atividade.service.impl;

import br.com.atividade.mapper.AtividadeMapper;
import br.com.atividade.model.Atividade;
import br.com.atividade.repository.AtividadeRepository;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import br.com.atividade.sevice.impl.AtividadeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AtividadeServiceImpl")
class AtividadeServiceImplTest {

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private AtividadeMapper atividadeMapper;

    @InjectMocks
    private AtividadeServiceImpl atividadeService;

    private AtividadeInput atividadeInputValida;
    private Atividade atividade;
    private AtividadeOutput atividadeOutput;

    @BeforeEach
    void setUp() {
        atividadeInputValida = new AtividadeInput();
        atividadeInputValida.setFuncional("EMP001");
        atividadeInputValida.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividadeInputValida.setCodigoAtividade("RUN");
        atividadeInputValida.setDescricaoAtividade("Corrida matinal de 5km");

        atividade = new Atividade();
        atividade.setIdAtividade(1L);
        atividade.setFuncional("EMP001");
        atividade.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividade.setCodigoAtividade("RUN");
        atividade.setDescricaoAtividade("Corrida matinal de 5km");

        atividadeOutput = new AtividadeOutput();
        atividadeOutput.setIdAtividade(1L);
        atividadeOutput.setFuncional("EMP001");
        atividadeOutput.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividadeOutput.setCodigoAtividade("RUN");
        atividadeOutput.setDescricaoAtividade("Corrida matinal de 5km");
    }

    @Test
    @DisplayName("Deve criar atividade com sucesso")
    void deveCriarAtividadeComSucesso() {
        when(atividadeMapper.toEntity(atividadeInputValida)).thenReturn(atividade);
        when(atividadeRepository.save(any(Atividade.class))).thenReturn(atividade);
        when(atividadeMapper.toOutput(atividade)).thenReturn(atividadeOutput);

        AtividadeOutput result = atividadeService.criarAtividade(atividadeInputValida);

        assertThat(result).isNotNull();
        assertThat(result.getIdAtividade()).isEqualTo(1L);
        assertThat(result.getFuncional()).isEqualTo("EMP001");
        assertThat(result.getCodigoAtividade()).isEqualTo("RUN");

        verify(atividadeMapper).toEntity(atividadeInputValida);
        verify(atividadeRepository).save(any(Atividade.class));
        verify(atividadeMapper).toOutput(atividade);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atividade com input nulo")
    void deveLancarExcecaoAoCriarAtividadeComInputNulo() {
        assertThatThrownBy(() -> atividadeService.criarAtividade(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("atividadeInput");

        verifyNoInteractions(atividadeMapper, atividadeRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atividade sem data/hora")
    void deveLancarExcecaoAoCriarAtividadeSemDataHora() {
        AtividadeInput inputInvalido = new AtividadeInput();
        inputInvalido.setFuncional("EMP001");
        inputInvalido.setDataHora(null); // Data nula
        inputInvalido.setCodigoAtividade("RUN");
        inputInvalido.setDescricaoAtividade("Corrida matinal de 5km");

        assertThatThrownBy(() -> atividadeService.criarAtividade(inputInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data/hora da atividade deve ser informada");

        verifyNoInteractions(atividadeMapper, atividadeRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atividade sem funcional")
    void deveLancarExcecaoAoCriarAtividadeSemFuncional() {
        AtividadeInput inputInvalido = new AtividadeInput();
        inputInvalido.setFuncional(""); // Funcional vazio
        inputInvalido.setDataHora(LocalDateTime.now().plusDays(1));
        inputInvalido.setCodigoAtividade("RUN");
        inputInvalido.setDescricaoAtividade("Corrida matinal de 5km");

        assertThatThrownBy(() -> atividadeService.criarAtividade(inputInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Funcional deve ser informado");

        verifyNoInteractions(atividadeMapper, atividadeRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atividade sem código")
    void deveLancarExcecaoAoCriarAtividadeSemCodigo() {
        AtividadeInput inputInvalido = new AtividadeInput();
        inputInvalido.setFuncional("EMP001");
        inputInvalido.setDataHora(LocalDateTime.now().plusDays(1));
        inputInvalido.setCodigoAtividade(""); // Código vazio
        inputInvalido.setDescricaoAtividade("Corrida matinal de 5km");

        assertThatThrownBy(() -> atividadeService.criarAtividade(inputInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código da atividade deve ser informado");

        verifyNoInteractions(atividadeMapper, atividadeRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atividade sem descrição")
    void deveLancarExcecaoAoCriarAtividadeSemDescricao() {
        AtividadeInput inputInvalido = new AtividadeInput();
        inputInvalido.setFuncional("EMP001");
        inputInvalido.setDataHora(LocalDateTime.now().plusDays(1));
        inputInvalido.setCodigoAtividade("RUN");
        inputInvalido.setDescricaoAtividade(""); // Descrição vazia

        assertThatThrownBy(() -> atividadeService.criarAtividade(inputInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Descrição da atividade deve ser informada");

        verifyNoInteractions(atividadeMapper, atividadeRepository);
    }

    @Test
    @DisplayName("Deve listar todas as atividades com sucesso")
    void deveListarTodasAtividadesComSucesso() {
        List<Atividade> atividades = Arrays.asList(atividade);
        List<AtividadeOutput> atividadeOutputs = Arrays.asList(atividadeOutput);

        when(atividadeRepository.findAll()).thenReturn(atividades);
        when(atividadeMapper.toOutputList(atividades)).thenReturn(atividadeOutputs);

        List<AtividadeOutput> result = atividadeService.listarTodasAtividades();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFuncional()).isEqualTo("EMP001");

        verify(atividadeRepository).findAll();
        verify(atividadeMapper).toOutputList(atividades);
    }

    @Test
    @DisplayName("Deve listar atividades por funcional com sucesso")
    void deveListarAtividadesPorFuncionalComSucesso() {
        String funcional = "EMP001";
        List<Atividade> atividades = Arrays.asList(atividade);
        List<AtividadeOutput> atividadeOutputs = Arrays.asList(atividadeOutput);

        when(atividadeRepository.findByFuncional(funcional)).thenReturn(atividades);
        when(atividadeMapper.toOutputList(atividades)).thenReturn(atividadeOutputs);

        List<AtividadeOutput> result = atividadeService.listarAtividadesPorFuncional(funcional);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFuncional()).isEqualTo("EMP001");

        verify(atividadeRepository).findByFuncional(funcional);
        verify(atividadeMapper).toOutputList(atividades);
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar por funcional nulo")
    void deveLancarExcecaoAoListarPorFuncionalNulo() {
        assertThatThrownBy(() -> atividadeService.listarAtividadesPorFuncional(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Funcional deve ser informado");

        verifyNoInteractions(atividadeRepository, atividadeMapper);
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar por funcional vazio")
    void deveLancarExcecaoAoListarPorFuncionalVazio() {
        assertThatThrownBy(() -> atividadeService.listarAtividadesPorFuncional(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Funcional deve ser informado");

        verifyNoInteractions(atividadeRepository, atividadeMapper);
    }

    @Test
    @DisplayName("Deve listar atividades com filtros aplicados")
    void deveListarAtividadesComFiltros() {
        String funcional = "EMP001";
        String codigoAtividade = "RUN";
        String descricaoAtividade = "corrida";
        LocalDate dataInicio = LocalDate.of(2025, 9, 1);
        LocalDate dataFim = LocalDate.of(2025, 9, 30);

        List<Atividade> atividades = Arrays.asList(atividade);

        when(atividadeRepository.findAll(any(Specification.class))).thenReturn(atividades);
        when(atividadeMapper.toOutput(atividade)).thenReturn(atividadeOutput);

        List<AtividadeOutput> result = atividadeService.listarAtividadesComFiltros(
                funcional, codigoAtividade, descricaoAtividade, dataInicio, dataFim);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFuncional()).isEqualTo("EMP001");

        verify(atividadeRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve buscar atividade por ID com sucesso")
    void deveBuscarAtividadePorIdComSucesso() {
        Long id = 1L;
        when(atividadeRepository.findById(id)).thenReturn(Optional.of(atividade));
        when(atividadeMapper.toOutput(atividade)).thenReturn(atividadeOutput);

        Optional<AtividadeOutput> result = atividadeService.buscarAtividadePorId(id);

        assertThat(result).isPresent();
        assertThat(result.get().getIdAtividade()).isEqualTo(id);
        assertThat(result.get().getFuncional()).isEqualTo("EMP001");

        verify(atividadeRepository).findById(id);
        verify(atividadeMapper).toOutput(atividade);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando atividade não encontrada por ID")
    void deveRetornarOptionalVazioQuandoAtividadeNaoEncontrada() {
        Long id = 999L;
        when(atividadeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<AtividadeOutput> result = atividadeService.buscarAtividadePorId(id);

        assertThat(result).isEmpty();

        verify(atividadeRepository).findById(id);
        verifyNoInteractions(atividadeMapper);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID nulo")
    void deveLancarExcecaoAoBuscarPorIdNulo() {
        assertThatThrownBy(() -> atividadeService.buscarAtividadePorId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID deve ser um número positivo");

        verifyNoInteractions(atividadeRepository, atividadeMapper);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inválido")
    void deveLancarExcecaoAoBuscarPorIdInvalido() {
        assertThatThrownBy(() -> atividadeService.buscarAtividadePorId(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID deve ser um número positivo");

        verifyNoInteractions(atividadeRepository, atividadeMapper);
    }

    @Test
    @DisplayName("Deve atualizar atividade com sucesso")
    void deveAtualizarAtividadeComSucesso() {
        Long id = 1L;
        when(atividadeRepository.findById(id)).thenReturn(Optional.of(atividade));
        when(atividadeRepository.existsById(id)).thenReturn(true);
        when(atividadeRepository.save(any(Atividade.class))).thenReturn(atividade);
        when(atividadeMapper.toOutput(atividade)).thenReturn(atividadeOutput);

        AtividadeOutput result = atividadeService.atualizarAtividade(id, atividadeInputValida);

        assertThat(result).isNotNull();
        assertThat(result.getIdAtividade()).isEqualTo(id);

        verify(atividadeRepository).existsById(id);
        verify(atividadeRepository).findById(id);
        verify(atividadeMapper).updateEntityFromInput(atividadeInputValida, atividade);
        verify(atividadeRepository).save(atividade);
        verify(atividadeMapper).toOutput(atividade);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com ID nulo")
    void deveLancarExcecaoAoAtualizarComIdNulo() {
        assertThatThrownBy(() -> atividadeService.atualizarAtividade(null, atividadeInputValida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID deve ser um número positivo");

        verifyNoInteractions(atividadeRepository, atividadeMapper);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar atividade inexistente")
    void deveLancarExcecaoAoAtualizarAtividadeInexistente() {
        Long id = 999L;
        when(atividadeRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> atividadeService.atualizarAtividade(id, atividadeInputValida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Atividade com ID " + id + " não encontrada");

        verify(atividadeRepository).existsById(id);
        verifyNoInteractions(atividadeMapper);
    }

    @Test
    @DisplayName("Deve deletar atividade com sucesso")
    void deveDeletarAtividadeComSucesso() {
        Long id = 1L;
        when(atividadeRepository.existsById(id)).thenReturn(true);

        atividadeService.deletarAtividade(id);

        verify(atividadeRepository).existsById(id);
        verify(atividadeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar com ID nulo")
    void deveLancarExcecaoAoDeletarComIdNulo() {
        assertThatThrownBy(() -> atividadeService.deletarAtividade(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID deve ser um número positivo");

        verifyNoInteractions(atividadeRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar atividade inexistente")
    void deveLancarExcecaoAoDeletarAtividadeInexistente() {
        Long id = 999L;
        when(atividadeRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> atividadeService.deletarAtividade(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Atividade com ID " + id + " não encontrada");

        verify(atividadeRepository).existsById(id);
        verify(atividadeRepository, never()).deleteById(any());
    }
}
