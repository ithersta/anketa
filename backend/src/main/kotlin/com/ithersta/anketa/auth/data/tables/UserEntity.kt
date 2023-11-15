package com.ithersta.anketa.auth.data.tables

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "users")
class UserEntity(
    @Id
    val id: UUID? = null,
)
