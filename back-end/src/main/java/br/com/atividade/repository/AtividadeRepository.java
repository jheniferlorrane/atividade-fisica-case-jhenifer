package br.com.atividade.repository;

import br.com.atividade.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    
    @Query("SELECT a FROM Atividade a WHERE a.funcional = :funcional")
    List<Atividade> findByFuncional(@Param("funcional") String funcional);
    
    @Query("SELECT a FROM Atividade a WHERE " +
           "(:funcional IS NULL OR a.funcional = :funcional) AND " +
           "(:codigoAtividade IS NULL OR a.codigoAtividade = :codigoAtividade) AND " +
           "(:descricaoAtividade IS NULL OR LOWER(a.descricaoAtividade) LIKE LOWER(CONCAT('%', :descricaoAtividade, '%')))")
    List<Atividade> findWithFilters(@Param("funcional") String funcional,
                                   @Param("codigoAtividade") String codigoAtividade,
                                   @Param("descricaoAtividade") String descricaoAtividade);
    
    boolean existsByCodigoAtividadeAndFuncional(String codigoAtividade, String funcional);
}