package br.com.atividade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes de Integração da Aplicação")
class AtividadeApplicationTests {

	@Test
	@DisplayName("Deve carregar contexto da aplicação com sucesso")
	void contextLoads() {
	}
}
