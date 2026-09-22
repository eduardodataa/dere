package br.gov.dere.domain.layout;

public interface LayoutDefinition<T> extends DereLayoutHandler<T> {
  String schemaLocation();
}
