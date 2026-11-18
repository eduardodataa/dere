package br.gov.dere.application.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadS3Configuration {
  @Bean
  ArmazenamentoObjetoUpload armazenamentoObjetoUpload(
      @Value("${dere.deployment:local}") String deployment,
      @Value("${dere.upload.s3.enabled:}") String enabled,
      @Value("${dere.upload.s3.bucket:dataa-dere}") String bucket,
      @Value("${dere.upload.s3.region:sa-east-1}") String region,
      @Value("${dere.upload.s3.profile:fenix2s3}") String profile,
      @Value("${dere.upload.diretorio:data/objetos}") String diretorio) {
    if (usarS3(enabled, deployment)) {
      return new ArmazenamentoComReservaLocal(new S3ArmazenamentoObjetoUpload(bucket, region, profile), java.nio.file.Path.of(diretorio));
    }
    return new MemoriaArmazenamentoObjetoUpload(bucket);
  }

  static boolean usarS3(String enabled, String deployment) {
    if (enabled != null && !enabled.isBlank()) return Boolean.parseBoolean(enabled.trim());
    var pasta = PastaAmbienteUpload.de(deployment);
    return !"local".equals(pasta);
  }
}
