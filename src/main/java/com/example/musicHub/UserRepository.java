package com.example.musicHub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.mbti = :mbti WHERE u.id = :userId")
    void updateMbtiById(long userId, String mbti);
}
