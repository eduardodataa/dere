package br.gov.dere.application.layout;

import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.LayoutDefinition;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DereLayoutRegistry {
  private final Map<LayoutCode, LayoutDefinition<?>> definitions;

  public DereLayoutRegistry(List<LayoutDefinition<?>> definitions) {
    this.definitions = definitions.stream()
        .collect(Collectors.toUnmodifiableMap(LayoutDefinition::layout, Function.identity()));
  }

  public LayoutDefinition<?> get(LayoutCode layout) {
    var definition = definitions.get(layout);
    if (definition == null) {
      throw new IllegalArgumentException("Layout não registrado: " + layout.displayName());
    }
    return definition;
  }

  public List<LayoutCode> availableLayouts() {
    return definitions.keySet().stream().sorted().toList();
  }
}
