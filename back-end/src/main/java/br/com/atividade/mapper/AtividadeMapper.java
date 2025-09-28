package br.com.atividade.mapper;

import br.com.atividade.model.Atividade;
import br.com.atividade.sevice.dto.input.AtividadeInput;
import br.com.atividade.sevice.dto.output.AtividadeOutput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AtividadeMapper {
    
    Atividade toEntity(AtividadeInput input);
    
    AtividadeOutput toOutput(Atividade entity);
    
    List<AtividadeOutput> toOutputList(List<Atividade> entities);
    
    void updateEntityFromInput(AtividadeInput input, @MappingTarget Atividade entity);
}