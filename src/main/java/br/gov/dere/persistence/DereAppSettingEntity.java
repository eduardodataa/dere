package br.gov.dere.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "dere_app_setting")
public class DereAppSettingEntity {
  @Id
  @Column(name = "setting_key", length = 100)
  private String settingKey;
  @Lob
  @Column(name = "setting_value")
  private String settingValue;
  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  protected DereAppSettingEntity() {}

  public DereAppSettingEntity(String settingKey, String settingValue) {
    this.settingKey = settingKey;
    this.settingValue = settingValue;
  }

  public void value(String settingValue) {
    this.settingValue = settingValue;
    this.updatedAt = Instant.now();
  }

  public String getSettingKey() {
    return settingKey;
  }

  public String getSettingValue() {
    return settingValue;
  }
}
