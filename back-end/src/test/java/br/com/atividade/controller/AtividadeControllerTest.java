package br.com.atividade.controller;

import br.com.atividade.sevice.AtividadeService;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtividadeController.class)
@DisplayName("Testes do AtividadeController")
class AtividadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtividadeService atividadeService;

    @Autowired
    private ObjectMapper objectMapper;

    private AtividadeInput atividadeInputValida;
    private AtividadeOutput atividadeOutput;

    @BeforeEach
    void setUp() {
        atividadeInputValida = new AtividadeInput();
        atividadeInputValida.setFuncional("EMP001");
        atividadeInputValida.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividadeInputValida.setCodigoAtividade("RUN");
        atividadeInputValida.setDescricaoAtividade("Corrida matinal de 5km");

        atividadeOutput = new AtividadeOutput();
        atividadeOutput.setIdAtividade(1L);
        atividadeOutput.setFuncional("EMP001");
        atividadeOutput.setDataHora(LocalDateTime.of(2025, 12, 25, 10, 30));
        atividadeOutput.setCodigoAtividade("RUN");
        atividadeOutput.setDescricaoAtividade("Corrida matinal de 5km");
    }

    @Test
    @DisplayName("Deve criar atividade com dados válidos e retornar 201 Created")
    void deveCriarAtividadeComDadosValidosERetornar201() throws Exception {
        when(atividadeService.criarAtividade(any(AtividadeInput.class))).thenReturn(atividadeOutput);

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAtividade").value(1L))
                .andExpect(jsonPath("$.funcional").value("EMP001"))
                .andExpect(jsonPath("$.codigoAtividade").value("RUN"))
                .andExpect(jsonPath("$.descricaoAtividade").value("Corrida matinal de 5km"));

        verify(atividadeService).criarAtividade(any(AtividadeInput.class));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando dados inválidos são enviados")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        AtividadeInput inputInvalido = new AtividadeInput();
        inputInvalido.setFuncional(""); 
        inputInvalido.setDataHora(null);
        inputInvalido.setCodigoAtividade(""); 
        inputInvalido.setDescricaoAtividade(""); 

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando service lança IllegalArgumentException")
    void deveRetornar400QuandoServiceLancaIllegalArgumentException() throws Exception {
        when(atividadeService.criarAtividade(any(AtividadeInput.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 500 quando service lança Exception genérica")
    void deveRetornar500QuandoServiceLancaException() throws Exception {
        when(atividadeService.criarAtividade(any(AtividadeInput.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve listar atividades sem filtros e retornar 200 OK")
    void deveListarAtividadesSemFiltros() throws Exception {
        List<AtividadeOutput> atividades = Arrays.asList(atividadeOutput);
        when(atividadeService.listarAtividadesComFiltros(null, null, null, null, null))
                .thenReturn(atividades);

        mockMvc.perform(get("/atividades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idAtividade").value(1L))
                .andExpect(jsonPath("$[0].funcional").value("EMP001"));
    }

    @Test
    @DisplayName("Deve listar atividades com filtros aplicados")
    void deveListarAtividadesComFiltros() throws Exception {
        List<AtividadeOutput> atividades = Arrays.asList(atividadeOutput);
        when(atividadeService.listarAtividadesComFiltros(eq("EMP001"), eq("RUN"), eq("corrida"), 
                any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(atividades);

        mockMvc.perform(get("/atividades")
                .param("funcional", "EMP001")
                .param("codigoAtividade", "RUN")
                .param("descricaoAtividade", "corrida")
                .param("dataInicio", "2025-09-01")
                .param("dataFim", "2025-09-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].funcional").value("EMP001"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhuma atividade é encontrada")
    void deveRetornarListaVaziaQuandoNenhumaAtividadeEncontrada() throws Exception {
        when(atividadeService.listarAtividadesComFiltros(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/atividades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 400 ao listar com filtros inválidos")
    void deveRetornar400AoListarComFiltrosInvalidos() throws Exception {
        when(atividadeService.listarAtividadesComFiltros(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Filtros inválidos"));

        mockMvc.perform(get("/atividades")
                .param("funcional", "EMP001"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve buscar atividade por ID e retornar 200 OK")
    void deveBuscarAtividadePorIdERetornar200() throws Exception {
        when(atividadeService.buscarAtividadePorId(1L)).thenReturn(Optional.of(atividadeOutput));

        mockMvc.perform(get("/atividades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAtividade").value(1L))
                .andExpect(jsonPath("$.funcional").value("EMP001"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando atividade não é encontrada por ID")
    void deveRetornar404QuandoAtividadeNaoEncontrada() throws Exception {
        when(atividadeService.buscarAtividadePorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/atividades/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 500 ao buscar por ID quando service lança exception")
    void deveRetornar500AoBuscarPorIdQuandoServiceLancaException() throws Exception {
        when(atividadeService.buscarAtividadePorId(1L))
                .thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(get("/atividades/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve atualizar atividade e retornar 200 OK")
    void deveAtualizarAtividadeERetornar200() throws Exception {
        when(atividadeService.atualizarAtividade(eq(1L), any(AtividadeInput.class)))
                .thenReturn(atividadeOutput);

        mockMvc.perform(put("/atividades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAtividade").value(1L))
                .andExpect(jsonPath("$.funcional").value("EMP001"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao atualizar com dados inválidos")
    void deveRetornar400AoAtualizarComDadosInvalidos() throws Exception {
        when(atividadeService.atualizarAtividade(eq(1L), any(AtividadeInput.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(put("/atividades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar atividade inexistente")
    void deveRetornar404AoAtualizarAtividadeInexistente() throws Exception {
        when(atividadeService.atualizarAtividade(eq(999L), any(AtividadeInput.class)))
                .thenThrow(new RuntimeException("Atividade não encontrada"));

        mockMvc.perform(put("/atividades/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atividadeInputValida)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar atividade e retornar 204 No Content")
    void deveDeletarAtividadeERetornar204() throws Exception {
        doNothing().when(atividadeService).deletarAtividade(1L);

        mockMvc.perform(delete("/atividades/1"))
                .andExpect(status().isNoContent());

        verify(atividadeService).deletarAtividade(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao deletar atividade inexistente")
    void deveRetornar404AoDeletarAtividadeInexistente() throws Exception {
        doThrow(new RuntimeException("Atividade não encontrada"))
                .when(atividadeService).deletarAtividade(999L);

                mockMvc.perform(delete("/atividades/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 500 ao deletar quando service lança exception genérica")
    void deveRetornar500AoDeletarQuandoServiceLancaException() throws Exception {
        doThrow(new RuntimeException("Erro interno"))
                .when(atividadeService).deletarAtividade(1L);

                mockMvc.perform(delete("/atividades/1"))
                .andExpect(status().isInternalServerError());
    }
}
