package com.eCommerce.repository;

import com.eCommerce.domain.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRole, Long> {
}
