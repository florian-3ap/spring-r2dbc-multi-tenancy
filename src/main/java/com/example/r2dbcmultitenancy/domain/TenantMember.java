package com.example.r2dbcmultitenancy.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Table("tenant_member")
public class TenantMember extends BaseEntity {

  @Id
  @Column("id")
  private Long id;

  @Column("first_name")
  private String firstName;

  @Column("last_name")
  private String lastName;
}
