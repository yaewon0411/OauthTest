package com.my.oauthtest.domain.user;

import com.my.oauthtest.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String primaryEmail;

    @Column(nullable = false)
    private String primaryName;

    @Builder
    public User(String primaryEmail, String primaryName) {
        this.primaryEmail = primaryEmail;
        this.primaryName = primaryName;
    }
}
