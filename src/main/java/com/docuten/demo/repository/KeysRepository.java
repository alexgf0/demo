package com.docuten.demo.repository;

import com.docuten.demo.model.Keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KeysRepository extends JpaRepository<Keys, UUID> {
}
