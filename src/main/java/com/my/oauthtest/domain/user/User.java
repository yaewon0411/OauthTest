package com.my.oauthtest.domain.user;

import com.my.oauthtest.domain.user.oauth.Provider;
import com.my.oauthtest.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 127)
    private String name;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Builder
    public User(Long id, String email, String name, String providerId, Provider provider) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.providerId = providerId;
        this.provider = provider;
    }
}
