package com.eCommerce.repository;

import org.springframework.data.repository.CrudRepository;

import com.eCommerce.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Role findByname(String name);
}
