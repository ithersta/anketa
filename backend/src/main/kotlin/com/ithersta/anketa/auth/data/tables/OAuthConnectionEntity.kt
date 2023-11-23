package com.ithersta.anketa.auth.data.tables

import com.ithersta.anketa.auth.OAuthProvider
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.util.*

@Table(name = "oauth_connections")
class OAuthConnectionEntity(
    val provider: OAuthProvider,
    @Column("provider_user_id")
    val providerUserId: String,
    @Column("user_id")
    val userId: UUID,
) {
    @Id
    var id: UUID? = null
}
