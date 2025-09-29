import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { format } from "date-fns";
import "./FormularioAtividade.css";

const FormularioAtividade = ({ atividade, onSalvar, onCancelar, titulo }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    setValue,
  } = useForm();

  const [carregando, setCarregando] = useState(false);

  // Se é edição, preenche o formulário
  useEffect(() => {
    if (atividade) {
      setValue("funcional", atividade.funcional);
      setValue("codigoAtividade", atividade.codigoAtividade);
      setValue("descricaoAtividade", atividade.descricaoAtividade);

      // Formatar data para input datetime-local
      if (atividade.dataHora) {
        const dataFormatada = format(
          new Date(atividade.dataHora),
          "yyyy-MM-dd'T'HH:mm"
        );
        setValue("dataHora", dataFormatada);
      }
    }
  }, [atividade, setValue]);

  const onSubmit = async (dados) => {
    setCarregando(true);
    try {
      // Converter data para formato ISO
      const atividadeData = {
        ...dados,
        dataHora: new Date(dados.dataHora).toISOString().slice(0, 19),
      };

      await onSalvar(atividadeData);
      if (!atividade) {
        reset(); // Limpa formulário apenas se for criação
      }
    } catch (error) {
      console.error("Erro ao salvar:", error);
      alert("Erro ao salvar atividade. Verifique os dados e tente novamente.");
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="formulario-overlay">
      <div className="formulario-container">
        <h2>{titulo}</h2>

        <form onSubmit={handleSubmit(onSubmit)} className="formulario">
          <div className="campo">
            <label htmlFor="funcional">Funcional*</label>
            <input
              id="funcional"
              type="text"
              maxLength="50"
              {...register("funcional", {
                required: "Funcional é obrigatório",
                maxLength: { value: 50, message: "Máximo 50 caracteres" },
              })}
              placeholder="Ex: 12345"
            />
            {errors.funcional && (
              <span className="erro">{errors.funcional.message}</span>
            )}
          </div>

          <div className="campo">
            <label htmlFor="codigoAtividade">Código da Atividade*</label>
            <input
              id="codigoAtividade"
              type="text"
              maxLength="20"
              {...register("codigoAtividade", {
                required: "Código da atividade é obrigatório",
                maxLength: { value: 20, message: "Máximo 20 caracteres" },
              })}
              placeholder="Ex: ACT001, Yoga, Corrida"
            />
            {errors.codigoAtividade && (
              <span className="erro">{errors.codigoAtividade.message}</span>
            )}
          </div>

          <div className="campo">
            <label htmlFor="descricaoAtividade">Descrição da Atividade*</label>
            <textarea
              id="descricaoAtividade"
              maxLength="255"
              rows="3"
              {...register("descricaoAtividade", {
                required: "Descrição é obrigatória",
                maxLength: { value: 255, message: "Máximo 255 caracteres" },
              })}
              placeholder="Ex: Yoga matinal no parque com exercícios de respiração"
            />
            {errors.descricaoAtividade && (
              <span className="erro">{errors.descricaoAtividade.message}</span>
            )}
          </div>

          <div className="campo">
            <label htmlFor="dataHora">Data e Hora*</label>
            <input
              id="dataHora"
              type="datetime-local"
              {...register("dataHora", {
                required: "Data e hora são obrigatórias",
              })}
            />
            {errors.dataHora && (
              <span className="erro">{errors.dataHora.message}</span>
            )}
          </div>

          <div className="botoes">
            <button
              type="button"
              onClick={onCancelar}
              className="botao-cancelar"
              disabled={carregando}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="botao-salvar"
              disabled={carregando}
            >
              {carregando ? "Salvando..." : "Salvar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default FormularioAtividade;
