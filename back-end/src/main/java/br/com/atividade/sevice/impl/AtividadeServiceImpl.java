package br.com.atividade.sevice.impl;

import br.com.atividade.mapper.AtividadeMapper;
import br.com.atividade.model.Atividade;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import br.com.atividade.repository.AtividadeRepository;
import br.com.atividade.sevice.AtividadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AtividadeServiceImpl implements AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final AtividadeMapper atividadeMapper;

    @Override
    public AtividadeOutput criarAtividade(AtividadeInput atividadeInput) {
        log.info("[ServiceImpl] - Iniciando criação de atividade para funcional: {}", atividadeInput.getFuncional());
        
        validarAtividadeInput(atividadeInput);
        
        Atividade atividade = atividadeMapper.toEntity(atividadeInput);
        
        log.debug("[DB] - Iniciando persistência da atividade no banco");
        Atividade atividadeSalva = atividadeRepository.save(atividade);
        log.info("[DB] - Atividade persistida com sucesso - ID: {}, Tabela: atividade", atividadeSalva.getIdAtividade());
        log.info("[ServiceImpl] - Atividade criada com sucesso - ID: {}, Código: {}",
                atividadeSalva.getIdAtividade(), atividadeSalva.getCodigoAtividade());
        
        return atividadeMapper.toOutput(atividadeSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeOutput> listarTodasAtividades() {
        log.info("[ServiceImpl] - Listando todas as atividades");
        
        log.debug("[DB] - Executando SELECT * FROM atividade");
        List<Atividade> atividades = atividadeRepository.findAll();
        log.info("[DB] - Consulta executada - {} registros retornados", atividades.size());
        
        return atividadeMapper.toOutputList(atividades);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeOutput> listarAtividadesPorFuncional(String funcional) {
        log.info("[ServiceImpl] - Listando atividades para funcional: {}", funcional);
        
        if (funcional == null || funcional.trim().isEmpty()) {
            log.warn("[ServiceImpl] - Funcional não informado para listagem");
            throw new IllegalArgumentException("Funcional deve ser informado");
        }
        
        String funcionalTrimmed = funcional.trim();
        
        log.debug("[DB] - Executando SELECT * FROM atividade WHERE funcional = '{}'", funcionalTrimmed);
        List<Atividade> atividades = atividadeRepository.findByFuncional(funcionalTrimmed);
        log.info("[DB] - Consulta por funcional executada - {} registros encontrados", atividades.size());
        log.info("[ServiceImpl] - Encontradas {} atividades para funcional: '{}'", atividades.size(), funcionalTrimmed);
        
        atividades.forEach(atividade ->
            log.debug("[ServiceImpl] - Atividade encontrada: ID={}, Funcional='{}', Codigo='{}'", 
                atividade.getIdAtividade(), atividade.getFuncional(), atividade.getCodigoAtividade()));
        
        return atividadeMapper.toOutputList(atividades);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade, String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim) {
        log.info("[ServiceImpl] - Listando atividades com filtros - Funcional: {}, CodigoAtividade: {}, DescricaoAtividade: {}, DataInicio: {}, DataFim: {}",
                funcional, codigoAtividade, descricaoAtividade, dataInicio, dataFim);

        return atividadeRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (funcional != null && !funcional.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("funcional"), funcional));
            }

            if (codigoAtividade != null && !codigoAtividade.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("codigoAtividade"), codigoAtividade));
            }

            if (descricaoAtividade != null && !descricaoAtividade.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("descricaoAtividade"), "%" + descricaoAtividade + "%"));
            }

            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHora"), dataInicio.atStartOfDay()));
            }

            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHora"), dataFim.atTime(23, 59, 59)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream().map(atividadeMapper::toOutput).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AtividadeOutput> buscarAtividadePorId(Long id) {
        log.info("[ServiceImpl] - Buscando atividade com ID: {}", id);

        if (id == null || id <= 0) {
            log.warn("[ServiceImpl] - ID inválido: {}", id);
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        log.debug("[DB] - Executando SELECT * FROM atividade WHERE id_atividade = {}", id);
        Optional<Atividade> atividade = atividadeRepository.findById(id);

        if (atividade.isPresent()) {
            log.info("[DB] - Registro encontrado para ID: {}", id);
            log.debug("[ServiceImpl] - Atividade encontrada: {}", atividade.get().getCodigoAtividade());
            return Optional.of(atividadeMapper.toOutput(atividade.get()));
        } else {
            log.warn("[DB] - Nenhum registro encontrado para ID: {}", id);
            log.warn("[ServiceImpl] - Atividade com ID {} não encontrada", id);
            return Optional.empty();
        }
    }

    @Override
    public AtividadeOutput atualizarAtividade(Long id, AtividadeInput atividadeInput) {
        log.info("[ServiceImpl] - Atualizando atividade com ID: {}", id);

        if (id == null || id <= 0) {
            log.warn("[ServiceImpl] - ID inválido: {}", id);
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        validarAtividadeInput(atividadeInput);

        log.debug("[DB] - Verificando existência do registro com ID: {}", id);
        Optional<Atividade> atividadeExistente = atividadeRepository.findById(id);

        if (!atividadeRepository.existsById(id)) {
            log.warn("[DB] - Registro com ID {} não existe na tabela atividade", id);
            log.warn("[ServiceImpl] - Atividade com ID {} não encontrada para atualização", id);
            throw new IllegalArgumentException("Atividade com ID " + id + " não encontrada");
        }

        Atividade atividade = atividadeExistente.get();
        atividadeMapper.updateEntityFromInput(atividadeInput, atividade);

        log.debug("[DB] - Executando UPDATE na tabela atividade para ID: {}", id);
        Atividade atividadeAtualizada = atividadeRepository.save(atividade);
        log.info("[DB] - Registro atualizado com sucesso - ID: {}", id);
        log.info("[ServiceImpl] - Atividade com ID {} atualizada com sucesso", id);

        return atividadeMapper.toOutput(atividadeAtualizada);
    }

    @Override
    public void deletarAtividade(Long id) {
        log.info("[ServiceImpl] - Deletando atividade com ID: {}", id);

        if (id == null || id <= 0) {
            log.warn("[ServiceImpl] - ID inválido: {}", id);
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        log.debug("[DB] - Verificando existência do registro para exclusão - ID: {}", id);
        if (!atividadeRepository.existsById(id)) {
            log.warn("[DB] - Registro com ID {} não existe para exclusão", id);
            log.warn("[ServiceImpl] - Atividade com ID {} não encontrada para exclusão", id);
            throw new IllegalArgumentException("Atividade com ID " + id + " não encontrada");
        }

        log.debug("[DB] - Executando DELETE FROM atividade WHERE id_atividade = {}", id);
        atividadeRepository.deleteById(id);
        log.info("[DB] - Registro excluído com sucesso - ID: {}", id);
        log.info("[ServiceImpl] - Atividade com ID {} deletada com sucesso", id);
    }
    
    private void validarAtividadeInput(AtividadeInput atividadeInput) {
        if (atividadeInput == null) {
            throw new IllegalArgumentException("Dados da atividade devem ser informados");
        }
        
        if (atividadeInput.getDataHora() == null) {
            log.warn("[ServiceImpl] - Data/hora não informada para a atividade");
            throw new IllegalArgumentException("Data/hora da atividade deve ser informada");
        }
        
        if (atividadeInput.getFuncional() == null || atividadeInput.getFuncional().trim().isEmpty()) {
            log.warn("[ServiceImpl] - Funcional não informado para a atividade");
            throw new IllegalArgumentException("Funcional deve ser informado");
        }
        
        if (atividadeInput.getCodigoAtividade() == null || atividadeInput.getCodigoAtividade().trim().isEmpty()) {
            log.warn("[ServiceImpl] - Código da atividade não informado");
            throw new IllegalArgumentException("Código da atividade deve ser informado");
        }
        
        if (atividadeInput.getDescricaoAtividade() == null || atividadeInput.getDescricaoAtividade().trim().isEmpty()) {
            log.warn("[ServiceImpl] - Descrição da atividade não informada");
            throw new IllegalArgumentException("Descrição da atividade deve ser informada");
        }
    }
}