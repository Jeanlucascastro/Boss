package com.taker.boss.controllers

import com.taker.boss.dtos.LoginDTO
import com.taker.boss.dtos.Message
import com.taker.boss.dtos.RegisterDTO
import com.taker.boss.models.User
import com.taker.boss.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.SecureRandom
import java.util.*

@RestController
@RequestMapping("boss")
class AuthController(private val userService: UserService) {

    val key = ByteArray(64)
    @PostMapping("register")
    fun hello(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        var user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {


        SecureRandom().nextBytes(key)

        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found"))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("Wrong credentials"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, key).compact()

        var cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any>{

        try {
            if (jwt === null) {
                return ResponseEntity.status(401).body(Message("Unautenticated"))
            }
            val body = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(Message("Unautenticated"))
        }

    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
    var cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Success"))
    }




}