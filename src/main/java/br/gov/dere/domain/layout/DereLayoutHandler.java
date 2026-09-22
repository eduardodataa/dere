package br.gov.dere.domain.layout;

public interface DereLayoutHandler<T> {
  LayoutCode layout();

  LayoutVersion version();

  T parseXml(XmlDocument document) throws Exception;

  T parseCsv(CsvDocument document);

  ValidationResult validate(T model, ValidationContext context) throws Exception;

  XmlDocument generateXml(T model) throws Exception;

  CsvDocument generateCsv(T model);
}
