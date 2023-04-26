package com.taker.boss.controllers

import com.taker.boss.dtos.LoginDTO
import com.taker.boss.dtos.Message
import com.taker.boss.dtos.RegisterDTO
import com.taker.boss.models.User
import com.taker.boss.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("boss")
class AuthController(private val userService: UserService, private val env: Environment) {
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
        val secret = env.getProperty("jwt.secret")
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found"))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("Wrong credentials"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder().setIssuer(issuer).setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.ES512, "secret").compact()

        return ResponseEntity.ok(jwt)
    }
}