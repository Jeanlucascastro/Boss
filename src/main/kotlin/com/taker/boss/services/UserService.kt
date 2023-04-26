package com.taker.boss.services

import com.taker.boss.models.User
import com.taker.boss.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun save(user: User): User {
        return this.userRepository.save(user)
    }
}