package com.iljin.apiServer.core.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(Long id);
    List<User> findAllByLoginIdContains(String loginId);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByCompCdAndLoginId(String compCd, String loginId);

    Optional<User> findByCompCdAndLoginIdLike(String compCd, String loginId);

    @Modifying
    @Transactional
    void deleteByLoginId(String loginId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE A_USER a SET a.login_pw = :loginPw, a.modified_date = SYSDATE WHERE a.login_id = :loginId", nativeQuery = true)
    int updatePw(String loginId, String loginPw);

    @Query(value = "select * from a_user where login_id not in ('admin')", nativeQuery = true)
    List<User> findAllByIsAdminFalse();
}
