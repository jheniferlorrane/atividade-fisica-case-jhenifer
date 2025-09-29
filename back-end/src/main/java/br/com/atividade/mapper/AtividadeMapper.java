package br.com.atividade.mapper;

import br.com.atividade.model.Atividade;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AtividadeMapper {
    
    @Mapping(target = "idAtividade", ignore = true)
    Atividade toEntity(AtividadeInput input);
    
    @Mapping(target = "idAtividade", source = "idAtividade")
    AtividadeOutput toOutput(Atividade entity);
    
    List<AtividadeOutput> toOutputList(List<Atividade> entities);
    
    @Mapping(target = "idAtividade", ignore = true)
    void updateEntityFromInput(AtividadeInput input, @MappingTarget Atividade entity);
}