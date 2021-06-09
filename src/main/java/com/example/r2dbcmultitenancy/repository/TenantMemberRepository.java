package com.example.r2dbcmultitenancy.repository;

import com.example.r2dbcmultitenancy.domain.TenantMember;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TenantMemberRepository extends R2dbcRepository<TenantMember, Long> {}
