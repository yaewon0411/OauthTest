package com.my.oauthtest.domain.social;

import com.my.oauthtest.domain.base.BaseEntity;
import com.my.oauthtest.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_social_user_provider_id",
                columnNames = {"provider", "providerId"}
        )
})
public class SocialUser extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false, length = 127)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Builder

    public SocialUser(Long id, User user, String email, String providerId, String name, Provider provider) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.providerId = providerId;
        this.name = name;
        this.provider = provider;
    }
}
