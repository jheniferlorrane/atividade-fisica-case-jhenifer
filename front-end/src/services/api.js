import axios from "axios";

export const getBaseURL = () => {
  const hostname = window.location.hostname;
  const protocol = window.location.protocol;

  return `${protocol}//${hostname}:8080`;
};

export const getSwaggerURL = () => {
  return `${getBaseURL()}/swagger-ui.html`;
};

const api = axios.create({
  baseURL: getBaseURL(),
  headers: {
    "Content-Type": "application/json",
  },
});

export const atividadeService = {
  listar: async (filtros = {}) => {
    try {
      const params = new URLSearchParams();
      if (filtros.funcional) params.append("funcional", filtros.funcional);
      if (filtros.codigoAtividade)
        params.append("codigoAtividade", filtros.codigoAtividade);
      if (filtros.descricaoAtividade)
        params.append("descricaoAtividade", filtros.descricaoAtividade);
      if (filtros.dataInicio) params.append("dataInicio", filtros.dataInicio);
      if (filtros.dataFim) params.append("dataFim", filtros.dataFim);

      const response = await api.get(`/atividades?${params}`);

      if (Array.isArray(response.data)) {
        return response.data;
      } else {
        console.error("Resposta inesperada da API:", response.data);
        return [];
      }
    } catch (error) {
      console.error("Erro ao listar atividades:", error);
      return [];
    }
  },

  buscarPorId: async (id) => {
    const response = await api.get(`/atividades/${id}`);
    return response.data;
  },

  criar: async (atividade) => {
    const response = await api.post("/atividades", atividade);
    return response.data;
  },

  atualizar: async (id, atividade) => {
    const response = await api.put(`/atividades/${id}`, atividade);
    return response.data;
  },

  deletar: async (id) => {
    await api.delete(`/atividades/${id}`);
  },
};

export default api;
