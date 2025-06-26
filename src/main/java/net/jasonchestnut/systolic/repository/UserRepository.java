package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

