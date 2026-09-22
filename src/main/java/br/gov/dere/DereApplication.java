package br.gov.dere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DereApplication {
  public static void main(String[] args) { SpringApplication.run(DereApplication.class, args); }
}
