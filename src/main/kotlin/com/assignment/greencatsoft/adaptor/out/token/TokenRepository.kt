package com.assignment.greencatsoft.adaptor.out.token

import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<TokenEntity, String>
