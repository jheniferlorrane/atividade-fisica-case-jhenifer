package br.com.atividade.controller;

import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import br.com.atividade.sevice.AtividadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/atividades")
@Validated
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @PostMapping
    public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput atividadeInput) {
        log.info("[Controller] - Recebida requisição para criar atividade: {}", atividadeInput);
        try {
            AtividadeOutput atividade = atividadeService.criarAtividade(atividadeInput);
            log.info("[Controller] - Atividade criada com sucesso: {}", atividade);
            return new ResponseEntity<>(atividade, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception erro) {
            log.error("[Controller] - Erro interno ao criar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<AtividadeOutput>> listarAtividades(
            @RequestParam(required = false) String funcional,
            @RequestParam(required = false) String codigoAtividade,
            @RequestParam(required = false) String descricaoAtividade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        log.info("[Controller] - Listando atividades. Filtros - Funcional: {}, CodigoAtividade: {}, DescricaoAtividade: {}, DataInicio: {}, DataFim: {}", 
                funcional, codigoAtividade, descricaoAtividade, dataInicio, dataFim);
        
        try {
            List<AtividadeOutput> atividades = atividadeService.listarAtividadesComFiltros(funcional, codigoAtividade, descricaoAtividade, dataInicio, dataFim);
            log.info("[Controller] - Total de atividades encontradas: {}", atividades.size());
            return ResponseEntity.ok(atividades);
        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos para filtro: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception erro) {
            log.error("[Controller] - Erro ao listar atividades", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeOutput> buscarAtividadePorId(@PathVariable Long id) {
        log.info("[Controller] - Buscando atividade com ID: {}", id);
        try {
            Optional<AtividadeOutput> atividade = atividadeService.buscarAtividadePorId(id);
            if (atividade.isPresent()) {
                log.info("[Controller] - Atividade encontrada: {}", atividade.get());
                return ResponseEntity.ok(atividade.get());
            } else {
                log.warn("[Controller] - Atividade com ID {} não encontrada", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade não encontrada");
            }
        } catch (ResponseStatusException erro) {
            throw erro;
        } catch (Exception erro) {
            log.error("[Controller] - Erro ao buscar atividade por ID", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtividadeOutput> atualizarAtividade(@PathVariable Long id,
            @Valid @RequestBody AtividadeInput atividadeInput) {
        log.info("[Controller] - Atualizando atividade com ID: {}", id);
        try {
            AtividadeOutput atividadeAtualizada = atividadeService.atualizarAtividade(id, atividadeInput);
            log.info("[Controller] - Atividade atualizada com sucesso: {}", atividadeAtualizada);
            return ResponseEntity.ok(atividadeAtualizada);
        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                log.warn("[Controller] - Atividade com ID {} não encontrada", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade não encontrada");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        } catch (Exception erro) {
            log.error("[Controller] - Erro ao atualizar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Long id) {
        log.info("[Controller] - Deletando atividade com ID: {}", id);
        try {
            atividadeService.deletarAtividade(id);
            log.info("[Controller] - Atividade com ID {} deletada com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                log.warn("[Controller] - Atividade com ID {} não encontrada", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade não encontrada");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        } catch (Exception erro) {
            log.error("[Controller] - Erro ao deletar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }
}