package com.example.r2dbcmultitenancy.domain;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BaseEntity {

  @Column("created_at")
  @CreatedDate
  Instant createdAt;

  @Column("updated_at")
  @LastModifiedDate
  Instant updatedAt;
}
