package br.gov.dere.api;

import br.gov.dere.application.access.EmissorToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FiltroAutenticacao extends OncePerRequestFilter {
  private final EmissorToken tokens;

  public FiltroAutenticacao(EmissorToken tokens) {
    this.tokens = tokens;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !caminho(request).startsWith("/api/");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    if (publico(request)) {
      chain.doFilter(request, response);
      return;
    }
    var autorizacao = request.getHeader("Authorization");
    if (autorizacao == null || !autorizacao.regionMatches(true, 0, "Bearer ", 0, 7)) {
      negar(response);
      return;
    }
    try {
      var userId = tokens.usuario(autorizacao.substring(7).trim());
      chain.doFilter(new PedidoComUsuario(request, String.valueOf(userId)), response);
    } catch (ResponseStatusException ex) {
      if (ex.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) negar(response);
      else throw ex;
    }
  }

  private static boolean publico(HttpServletRequest request) {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
    var caminho = caminho(request);
    return ("GET".equalsIgnoreCase(request.getMethod()) && "/api/ambiente".equals(caminho))
        || ("POST".equalsIgnoreCase(request.getMethod()) && "/api/auth/login".equals(caminho));
  }

  private static String caminho(HttpServletRequest request) {
    var uri = request.getRequestURI();
    var contexto = request.getContextPath() == null ? "" : request.getContextPath();
    return uri.startsWith(contexto) ? uri.substring(contexto.length()) : uri;
  }

  private static void negar(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"message\":\"Sessão inválida ou expirada.\"}");
  }

  private static final class PedidoComUsuario extends HttpServletRequestWrapper {
    private final String userId;

    private PedidoComUsuario(HttpServletRequest request, String userId) {
      super(request);
      this.userId = userId;
    }

    @Override
    public String getHeader(String name) {
      if ("X-User-Id".equalsIgnoreCase(name)) return userId;
      return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
      if ("X-User-Id".equalsIgnoreCase(name)) return Collections.enumeration(java.util.List.of(userId));
      return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
      var nomes = new ArrayList<String>();
      var atuais = super.getHeaderNames();
      if (atuais != null) atuais.asIterator().forEachRemaining(nomes::add);
      if (nomes.stream().noneMatch(item -> item.equalsIgnoreCase("X-User-Id"))) nomes.add("X-User-Id");
      return Collections.enumeration(nomes);
    }
  }
}
