package com.example.demo.repo;

import com.example.demo.entity.Suite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuiteRepository extends JpaRepository<Suite, Long> {
}
