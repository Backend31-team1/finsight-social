package com.project.price.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name="asset_metadata")
@Entity
@Setter
@Getter
public class AssetMetadata {

  @Id
  @Column(length = 20)
  private String symbol;            // "005930.KS"

  @Column(nullable = false, length = 100)
  private String name;              // "Samsung Electronics Co Ltd"

  @Column(length = 50)
  private String exchange;          // "KRX"

  @Column(length = 50)
  private String country;           // "KR"

  @Column(length = 50)
  private String finnhubIndustry;   // "Technology"

  private Long marketCapitalization;      // 500000000000L
  private Double shareOutstanding;        // 100000000.0

  @Column(length = 200)
  private String logo;              // URL

  @Column(length = 200)
  private String weburl;            // Company website

  private LocalDate ipo;            // LocalDate.parse("1970-06-11")

  @UpdateTimestamp
  private LocalDateTime updatedAt;  // 마지막 갱신 시각


}
