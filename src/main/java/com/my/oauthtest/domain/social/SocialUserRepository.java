package com.my.oauthtest.domain.social;

import com.my.oauthtest.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    Optional<SocialUser> findByProviderAndProviderId(Provider provider, String providerId);

    boolean existsByProviderAndProviderId(Provider provider, String providerId);

}
