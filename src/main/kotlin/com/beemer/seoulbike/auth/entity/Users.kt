package com.beemer.seoulbike.auth.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "\"Users\"")
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    val userId: UUID? = null,

    @Column(name = "nikcname", nullable = false)
    val nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    val createdDate: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    val modifiedDate: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_type")
    val socialType: SocialType
)
