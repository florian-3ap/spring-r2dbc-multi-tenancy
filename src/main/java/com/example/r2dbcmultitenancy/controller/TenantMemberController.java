package com.example.r2dbcmultitenancy.controller;

import com.example.r2dbcmultitenancy.domain.TenantMember;
import com.example.r2dbcmultitenancy.repository.TenantMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant-member")
public class TenantMemberController {

  private final TenantMemberRepository tenantMemberRepository;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<TenantMember> createTenant() {
    return tenantMemberRepository.findAll();
  }
}
