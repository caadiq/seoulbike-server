package com.beemer.seoulbike.auth.entity

import jakarta.persistence.*

@Entity
@Table(name = "\"SocialType\"")
class SocialType(
    socialType: String
) {
    @Id
    @Column(name = "social_type", nullable = false)
    var socialType: String = socialType
        protected set

    @OneToOne(mappedBy = "socialType", cascade = [CascadeType.ALL])
    var users: Users? = null
}
