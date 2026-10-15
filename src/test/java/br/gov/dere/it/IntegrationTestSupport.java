package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserRepository;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
abstract class IntegrationTestSupport {
  @Autowired protected MockMvc mockMvc;
  @Autowired protected AccessService access;
  @Autowired protected DereUserRepository users;
  @Autowired protected DereEntityRepository entities;

  protected Long masterId;
  protected Long entityId;

  @BeforeEach
  void seed() {
    users.findAll().forEach(user -> {
      user.getEntities().clear();
      users.save(user);
    });
    users.deleteAll();
    entities.deleteAll();
    var entity = entities.save(new DereEntityEntity("01914904", "Entidade IT DeRE"));
    var master = access.create("eduardo", "eduardo86", "Eduardo IT");
    master.setMaster(true);
    master.getEntities().add(entity);
    master = users.save(master);
    masterId = master.getId();
    entityId = entity.getId();
  }

  protected ResultActions login(String login, String password) throws Exception {
    return mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}"));
  }

  protected ResultActions simulate(String layout, String origem, String arquivo, String conteudo) throws Exception {
    var json = """
        {"layout":"%s","origem":"%s","arquivo":"%s","conteudo":%s}
        """.formatted(layout, origem, arquivo, toJsonString(conteudo));
    return mockMvc.perform(post("/api/transmissions/simulate")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-User-Id", masterId)
        .header("X-Entity-Id", entityId)
        .content(json));
  }

  protected static String fixture(String relative) throws Exception {
    return Files.readString(Path.of("examples").resolve(relative), StandardCharsets.UTF_8);
  }

  protected static String withUniqueId(String csvOrXml) {
    var matcher = java.util.regex.Pattern.compile("DeRE[0-9]{38}").matcher(csvOrXml);
    if (!matcher.find()) return csvOrXml;
    var antigo = matcher.group();
    var novo = antigo.substring(0, 37) + String.format("%05d", Math.abs(System.nanoTime() % 100_000));
    return csvOrXml.replace(antigo, novo);
  }

  protected static String toJsonString(String value) {
    return "\"" + value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\r", "\\r")
        .replace("\n", "\\n")
        .replace("\t", "\\t") + "\"";
  }

  protected void assertLoginOk() throws Exception {
    login("eduardo", "eduardo86")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(masterId))
        .andExpect(jsonPath("$.master").value(true));
  }
}
