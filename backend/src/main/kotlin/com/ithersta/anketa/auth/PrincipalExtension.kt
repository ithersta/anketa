package com.ithersta.anketa.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.*

val UsernamePasswordAuthenticationToken.userId: UUID
    get() = principal as UUID
