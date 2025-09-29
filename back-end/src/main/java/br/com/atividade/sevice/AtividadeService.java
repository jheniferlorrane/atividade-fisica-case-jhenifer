package br.com.atividade.sevice;

import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AtividadeService {
    
    AtividadeOutput criarAtividade(AtividadeInput atividadeInput);
    
    List<AtividadeOutput> listarTodasAtividades();
    
    List<AtividadeOutput> listarAtividadesPorFuncional(String funcional);
    
    List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade, String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim);

    Optional<AtividadeOutput> buscarAtividadePorId(Long id);

    AtividadeOutput atualizarAtividade(Long id, AtividadeInput atividadeInput);

    void deletarAtividade(Long id);
}
