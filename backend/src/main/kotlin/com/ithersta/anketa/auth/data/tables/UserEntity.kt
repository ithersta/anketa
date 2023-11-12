package com.ithersta.anketa.auth.data.tables

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
)
