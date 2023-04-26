package com.taker.boss.controllers

import com.taker.boss.dtos.LoginDTO
import com.taker.boss.dtos.RegisterDTO
import com.taker.boss.models.User
import com.taker.boss.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("boss")
class AuthController(private val userService: UserService) {
    @PostMapping("register")
    fun hello(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        var user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
        println("user")
        println(user.toString())
        return ResponseEntity.ok(user)
    }
}