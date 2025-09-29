package br.com.atividade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes de Integração da Aplicação")
class AtividadeApplicationTests {

	@Test
	@DisplayName("Deve carregar contexto da aplicação com sucesso")
	void contextLoads() {
	}

	@Test
	@DisplayName("Deve executar método main com sucesso")
	void deveExecutarMetodoMainComSucesso() {
		try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
			ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
			mockedSpringApplication.when(() -> SpringApplication.run(eq(AtividadeApplication.class), any(String[].class)))
					.thenReturn(mockContext);

			String[] args = {"--spring.profiles.active=test"};
			AtividadeApplication.main(args);

			mockedSpringApplication.verify(() -> SpringApplication.run(eq(AtividadeApplication.class), eq(args)));
		}
	}
}
