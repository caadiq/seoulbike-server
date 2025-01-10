package com.beemer.seoulbike.auth.entity

import com.beemer.seoulbike.app.entity.FavoriteStations
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "\"Users\"")
class Users(
    nickname: String,
    email: String,
    password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    val userId: UUID? = null

    @Column(name = "nickname", nullable = false)
    var nickname: String = nickname
        protected set

    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: LocalDateTime? = null
        protected set

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    var modifiedDate: LocalDateTime? = null
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_type")
    var socialType: SocialType? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var favoriteStations: MutableList<FavoriteStations> = mutableListOf()
}