package com.project.Book.repository;

import com.project.Book.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByExpireTimeBefore(Timestamp timestamp);

    @Query(value = """
        select exists(select 1 from refresh_token rt where rt.id = :id and rt.expire_time > :time)
    """, nativeQuery = true)
    int checkExpireTimeById(@Param("id")String id, @Param("time") LocalDateTime time);
}
