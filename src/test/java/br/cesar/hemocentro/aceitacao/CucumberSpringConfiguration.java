package br.cesar.hemocentro.aceitacao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import br.cesar.hemocentro.TestcontainersConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;

/** Sobe a aplicação inteira (HTTP real numa porta aleatória) com PostgreSQL em container. */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class CucumberSpringConfiguration {
}
