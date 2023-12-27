package com.ithersta.anketa.auth.data.tables

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "users")
class UserEntity(
    @Column("display_name")
    val displayName: String,
    val email: String,
) {
    @Id
    var id: UUID? = null
}
