import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import "./ListaAtividades.css";

const ListaAtividades = ({ atividades, onEditar, onDeletar, carregando }) => {
  const formatarData = (dataHora) => {
    try {
      return format(new Date(dataHora), "dd/MM/yyyy 'às' HH:mm", {
        locale: ptBR,
      });
    } catch {
      return "Data inválida";
    }
  };

  const confirmarDelecao = (id, descricao) => {
    if (
      window.confirm(
        `Tem certeza que deseja deletar a atividade "${descricao}"?`
      )
    ) {
      onDeletar(id);
    }
  };

  if (carregando) {
    return (
      <div className="lista-container">
        <div className="carregando">
          <div className="spinner"></div>
          <p>Carregando atividades...</p>
        </div>
      </div>
    );
  }

  if (!Array.isArray(atividades)) {
    console.error("O valor de atividades não é um array:", atividades);
    return (
      <div className="lista-container">
        <p>
          Erro ao carregar as atividades. Verifique os dados retornados pela
          API.
        </p>
      </div>
    );
  }

  if (atividades.length === 0) {
    return (
      <div className="lista-container">
        <div className="vazio">
          <p>Nenhuma atividade encontrada</p>
          <small>Crie uma nova atividade ou ajuste os filtros de busca</small>
        </div>
      </div>
    );
  }

  return (
    <div className="lista-container">
      <div className="lista-header">
        <h3>Atividades Encontradas ({atividades.length})</h3>
      </div>

      <div className="atividades-grid">
        {atividades.map((atividade) => (
          <div key={atividade.idAtividade} className="atividade-card">
            <div className="card-header">
              <div className="funcional">
                <strong>Funcional:</strong> {atividade.funcional}
              </div>
              <div className="acoes">
                <button
                  onClick={() => onEditar(atividade)}
                  className="botao-editar"
                  title="Editar atividade"
                >
                  ✏️
                </button>
                <button
                  onClick={() =>
                    confirmarDelecao(
                      atividade.idAtividade,
                      atividade.descricaoAtividade
                    )
                  }
                  className="botao-deletar"
                  title="Deletar atividade"
                >
                  🗑️
                </button>
              </div>
            </div>

            <div className="card-body">
              <div className="codigo">
                <strong>Código:</strong> {atividade.codigoAtividade}
              </div>

              <div className="descricao">
                <strong>Descrição:</strong> {atividade.descricaoAtividade}
              </div>

              <div className="data-hora">
                <strong>Data/Hora:</strong> {formatarData(atividade.dataHora)}
              </div>
            </div>

            <div className="card-footer">
              <small>ID: {atividade.idAtividade}</small>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ListaAtividades;
