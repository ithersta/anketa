package com.ithersta.anketa.auth.data.repositories

import com.ithersta.anketa.auth.data.tables.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID>