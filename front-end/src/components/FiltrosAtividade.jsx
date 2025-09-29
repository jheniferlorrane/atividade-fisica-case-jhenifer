import { useState } from "react";
import "./FiltrosAtividade.css";

const FiltrosAtividade = ({ onFiltrar, onLimpar }) => {
  const [filtros, setFiltros] = useState({
    funcional: "",
    codigoAtividade: "",
    descricaoAtividade: "",
    dataInicio: "",
    dataFim: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFiltros((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFiltrar(filtros);
  };

  const handleLimpar = () => {
    setFiltros({
      funcional: "",
      codigoAtividade: "",
      descricaoAtividade: "",
      dataInicio: "",
      dataFim: "",
    });
    onLimpar();
  };

  return (
    <div className="filtros-container">
      <h3>🔍 Filtros de Busca</h3>

      <form onSubmit={handleSubmit} className="filtros-form">
        <div className="filtros-campos">
          <div className="campo-filtro">
            <label htmlFor="funcional">Funcional</label>
            <input
              id="funcional"
              name="funcional"
              type="text"
              value={filtros.funcional}
              onChange={handleChange}
              placeholder="Ex: 12345"
            />
          </div>

          <div className="campo-filtro">
            <label htmlFor="codigoAtividade">Código da Atividade</label>
            <input
              id="codigoAtividade"
              name="codigoAtividade"
              type="text"
              value={filtros.codigoAtividade}
              onChange={handleChange}
              placeholder="Ex: ACT001, Yoga"
            />
          </div>

          <div className="campo-filtro">
            <label htmlFor="descricaoAtividade">Descrição</label>
            <input
              id="descricaoAtividade"
              name="descricaoAtividade"
              type="text"
              value={filtros.descricaoAtividade}
              onChange={handleChange}
              placeholder="Ex: matinal, parque"
            />
          </div>

          <div className="campo-filtro">
            <label htmlFor="dataInicio">Data Início</label>
            <input
              id="dataInicio"
              name="dataInicio"
              type="date"
              value={filtros.dataInicio}
              onChange={handleChange}
            />
          </div>

          <div className="campo-filtro">
            <label htmlFor="dataFim">Data Fim</label>
            <input
              id="dataFim"
              name="dataFim"
              type="date"
              value={filtros.dataFim}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="filtros-botoes">
          <button type="button" onClick={handleLimpar} className="botao-limpar">
            Limpar
          </button>
          <button type="submit" className="botao-filtrar">
            Filtrar
          </button>
        </div>
      </form>
    </div>
  );
};

export default FiltrosAtividade;
