package com.my.oauthtest.domain.user;

import com.my.oauthtest.domain.user.oauth.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProvider(String email, Provider provider);
    Optional<User> findByEmail(String email);

}
