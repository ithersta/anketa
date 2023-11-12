package com.ithersta.anketa.auth.data.tables

import com.ithersta.anketa.auth.OAuthProvider
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@IdClass(OAuthConnectionId::class)
@Table(name = "oauth_connections")
class OAuthConnectionEntity(
    @Id
    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    val provider: OAuthProvider,

    @Id
    @Column(name = "provider_user_id", nullable = false)
    val providerUserId: String,

    @JoinColumn(name = "user_id", nullable = false)
    val userId: UUID,
)

class OAuthConnectionId(
    val provider: OAuthProvider? = null,
    val providerUserId: String? = null,
) : Serializable {
    constructor() : this(null, null)
}
