package com.project.Book.repository;

import com.project.Book.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByExpireTimeBefore(Timestamp timestamp);
}
