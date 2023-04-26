package com.taker.boss.repositories

import com.taker.boss.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
}