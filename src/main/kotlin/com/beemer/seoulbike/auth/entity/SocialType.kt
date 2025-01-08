package com.beemer.seoulbike.auth.entity

import jakarta.persistence.*

@Entity
@Table(name = "\"SocialType\"")
data class SocialType(
    @Id
    @Column(name = "social_type", nullable = false)
    val socialType: String,

    @OneToOne(mappedBy = "social", cascade = [CascadeType.ALL])
    var users: Users? = null
)
