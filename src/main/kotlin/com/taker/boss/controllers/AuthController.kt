package com.taker.boss.controllers

import com.taker.boss.dtos.RegisterDTO
import com.taker.boss.models.User
import com.taker.boss.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val userService: UserService) {
    @GetMapping("register")
    @RequestMapping("boss")
    fun hello(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        var user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        return ResponseEntity.ok(this.userService.save(user))
    }
}