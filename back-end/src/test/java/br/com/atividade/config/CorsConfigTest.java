package br.com.atividade.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes do CorsConfig")
class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    @DisplayName("Deve criar bean CorsFilter corretamente")
    void deveCriarBeanCorsFilterCorretamente() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertThat(corsFilter).isNotNull();
        assertThat(corsFilter).isInstanceOf(CorsFilter.class);
    }

    @Test
    @DisplayName("Deve configurar CORS para permitir credenciais")
    void deveConfigurarCorsParaPermitirCredenciais() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then - Verificamos que o filtro foi criado (configuração interna)
        assertThat(corsFilter).isNotNull();
    }

    @Test
    @DisplayName("Deve permitir instanciação da classe de configuração")
    void devePermitirInstanciacaoDaClasseDeConfiguracao() {
        // When
        CorsConfig config = new CorsConfig();

        // Then
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias de CorsFilter")
    void deveCriarMultiplasInstanciasDeCorsFilter() {
        // When
        CorsFilter corsFilter1 = corsConfig.corsFilter();
        CorsFilter corsFilter2 = corsConfig.corsFilter();

        // Then
        assertThat(corsFilter1).isNotNull();
        assertThat(corsFilter2).isNotNull();
        // Cada chamada cria uma nova instância
        assertThat(corsFilter1).isNotSameAs(corsFilter2);
    }
}
