package br.gov.dere.persistence;

import br.gov.dere.domain.EventStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="dere_event")
public class DereEventEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="event_type",nullable=false) private String eventType;
  @Column(name="event_identifier",nullable=false,unique=true,length=42) private String eventIdentifier;
  @Column(nullable=false) private String environment;
  @Column(name="reference_period") private String referencePeriod;
  @Column(name="operation_type") private String operationType;
  @Column(name="schema_version",nullable=false) private String schemaVersion;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private EventStatus status;
  @Lob @Column(name="xml_unsigned") private String xmlUnsigned;
  @Lob @Column(name="xml_signed") private String xmlSigned;
  @Column(name="receipt_number") private String receiptNumber;
  @Column(name="created_at",nullable=false) private Instant createdAt;
  private Instant sentAt; private Instant processedAt;
  protected DereEventEntity() {}
  public DereEventEntity(String type,String identifier,String environment,String version,String xml){eventType=type;eventIdentifier=identifier;this.environment=environment;schemaVersion=version;xmlUnsigned=xml;status=EventStatus.GENERATED;createdAt=Instant.now();}
  public Long getId(){return id;} public String getEventIdentifier(){return eventIdentifier;} public String getXmlUnsigned(){return xmlUnsigned;} public String getXmlSigned(){return xmlSigned;} public EventStatus getStatus(){return status;} public void signed(String xml){xmlSigned=xml;status=EventStatus.SIGNED;} public void sent(){status=EventStatus.PROTOCOL_RECEIVED;sentAt=Instant.now();} public void processed(EventStatus s,String receipt){status=s;receiptNumber=receipt;processedAt=Instant.now();}
}
