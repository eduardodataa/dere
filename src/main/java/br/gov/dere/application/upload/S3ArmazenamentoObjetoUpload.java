package br.gov.dere.application.upload;

import java.nio.charset.StandardCharsets;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3ArmazenamentoObjetoUpload implements ArmazenamentoObjetoUpload {
  private final String bucket;
  private final String region;
  private final String profile;
  private volatile S3Client client;

  public S3ArmazenamentoObjetoUpload(String bucket, String region, String profile) {
    if (bucket == null || bucket.isBlank()) throw new IllegalStateException("Bucket S3 não configurado");
    if (region == null || region.isBlank()) throw new IllegalStateException("Região S3 não configurada");
    if (profile == null || profile.isBlank()) throw new IllegalStateException("Profile S3 não configurado");
    this.bucket = bucket;
    this.region = region;
    this.profile = profile;
  }

  S3ArmazenamentoObjetoUpload(S3Client client, String bucket) {
    this.client = client;
    this.bucket = bucket;
    this.region = "";
    this.profile = "";
  }

  private S3Client cliente() {
    var atual = client;
    if (atual != null) return atual;
    synchronized (this) {
      if (client == null) {
        client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(ProfileCredentialsProvider.create(profile))
            .build();
      }
      return client;
    }
  }

  @Override
  public String gravar(String chave, byte[] conteudo, String contentType) {
    try {
      var put = PutObjectRequest.builder()
          .bucket(bucket)
          .key(chave)
          .contentType(contentType == null || contentType.isBlank() ? "application/octet-stream" : contentType)
          .build();
      cliente().putObject(put, RequestBody.fromBytes(conteudo));
      return ChaveObjetoUpload.uri(bucket, chave);
    } catch (RuntimeException ex) {
      throw new IllegalStateException("Falha ao gravar objeto no S3", ex);
    }
  }

  @Override
  public byte[] lerBytes(String uri) {
    try {
      var chave = ChaveObjetoUpload.chaveDeUri(uri, bucket);
      var get = GetObjectRequest.builder().bucket(bucket).key(chave).build();
      return cliente().getObjectAsBytes(get).asByteArray();
    } catch (RuntimeException ex) {
      throw new IllegalStateException("Falha ao ler objeto no S3", ex);
    }
  }

  @Override
  public String lerTexto(String uri) {
    return new String(lerBytes(uri), StandardCharsets.UTF_8);
  }
}
