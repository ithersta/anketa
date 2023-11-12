package com.ithersta.anketa.auth.data.repositories

import com.ithersta.anketa.auth.data.tables.OAuthConnectionEntity
import com.ithersta.anketa.auth.data.tables.OAuthConnectionId
import org.springframework.data.jpa.repository.JpaRepository

interface OAuthConnectionRepository : JpaRepository<OAuthConnectionEntity, OAuthConnectionId>