package br.cesar.hemocentro;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HemocentroApplication {

    public static void main(String[] args) {
        SpringApplication.run(HemocentroApplication.class, args);
    }

    /** Relógio injetável: as regras de data ficam testáveis sem depender do dia em que o teste roda. */
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
