import React, { useState, useEffect } from "react";
import { atividadeService, getBaseURL, getSwaggerURL } from "./services/api";
import FormularioAtividade from "./components/FormularioAtividade";
import FiltrosAtividade from "./components/FiltrosAtividade";
import ListaAtividades from "./components/ListaAtividades";
import "./App.css";

function App() {
  const [atividades, setAtividades] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [atividadeEditando, setAtividadeEditando] = useState(null);
  const [apiStatus, setApiStatus] = useState("checking");

  const verificarStatusAPI = async () => {
    try {
      await atividadeService.listar({});
      setApiStatus("online");
    } catch (error) {
      console.warn("API offline ou inacessível:", error.message);
      setApiStatus("offline");
    }
  };

  useEffect(() => {
    carregarAtividades();
    verificarStatusAPI();
  }, []);

  const carregarAtividades = async (filtros = {}) => {
    setCarregando(true);
    try {
      const dados = await atividadeService.listar(filtros);
      setAtividades(dados);
      setApiStatus("online");
    } catch (error) {
      console.error("Erro ao carregar atividades:", error);
      setApiStatus("offline");
    } finally {
      setCarregando(false);
    }
  };

  const handleSalvarAtividade = async (dadosAtividade) => {
    try {
      if (atividadeEditando) {
        await atividadeService.atualizar(
          atividadeEditando.idAtividade,
          dadosAtividade
        );
        alert("Atividade atualizada com sucesso!");
      } else {
        await atividadeService.criar(dadosAtividade);
        alert("Atividade criada com sucesso!");
      }

      await carregarAtividades();

      setMostrarFormulario(false);
      setAtividadeEditando(null);
    } catch (error) {
      console.error("Erro ao salvar atividade:", error);
      throw error;
    }
  };
  const handleEditarAtividade = (atividade) => {
    setAtividadeEditando(atividade);
    setMostrarFormulario(true);
  };

  const handleDeletarAtividade = async (id) => {
    try {
      await atividadeService.deletar(id);
      alert("Atividade deletada com sucesso!");
      await carregarAtividades();
    } catch (error) {
      console.error("Erro ao deletar atividade:", error);
      alert("Erro ao deletar atividade. Tente novamente.");
    }
  };

  const handleNovaAtividade = () => {
    setAtividadeEditando(null);
    setMostrarFormulario(true);
  };

  const handleCancelarFormulario = () => {
    setMostrarFormulario(false);
    setAtividadeEditando(null);
  };

  const handleFiltrar = (filtros) => {
    carregarAtividades(filtros);
  };

  const handleLimparFiltros = () => {
    carregarAtividades();
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>🏃‍♂️ Gerenciador de Atividades Físicas</h1>
        <p>Sistema para gerenciar atividades físicas de funcionários</p>
      </header>

      <main className="app-main">
        <div className="container">
          {/* Seção de Ações Principais */}
          <div className="secao-acoes">
            <div className="acoes-header">
              <h2>Gerenciar Atividades</h2>
              <button
                onClick={handleNovaAtividade}
                className="botao-nova-atividade"
              >
                ➕ Nova Atividade
              </button>
            </div>
          </div>

          {/* Filtros */}
          <FiltrosAtividade
            onFiltrar={handleFiltrar}
            onLimpar={handleLimparFiltros}
          />

          {/* Lista de Atividades */}
          <ListaAtividades
            atividades={atividades}
            onEditar={handleEditarAtividade}
            onDeletar={handleDeletarAtividade}
            carregando={carregando}
          />

          {/* Formulário Modal */}
          {mostrarFormulario && (
            <FormularioAtividade
              atividade={atividadeEditando}
              onSalvar={handleSalvarAtividade}
              onCancelar={handleCancelarFormulario}
              titulo={atividadeEditando ? "Editar Atividade" : "Nova Atividade"}
            />
          )}
        </div>
      </main>

      <footer className="app-footer">
        <p>
          API rodando em: <strong>{getBaseURL()}</strong>
          <span className={`api-status ${apiStatus}`}>
            {apiStatus === "online" && " ✅ Online"}
            {apiStatus === "offline" && " ❌ Offline"}
            {apiStatus === "checking" && " 🔄 Verificando..."}
          </span>
          {" | "}Swagger:{" "}
          <a href={getSwaggerURL()} target="_blank" rel="noopener noreferrer">
            Documentação da API
          </a>
        </p>
      </footer>
    </div>
  );
}

export default App;
