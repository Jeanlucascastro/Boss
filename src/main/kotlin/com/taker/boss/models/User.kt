package com.taker.boss.models

import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.swing.text.StyledEditorKit.BoldAction

@Entity
@Table(name = "tb_user")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column()
    var name = ""

    @Column(unique = true)
    var email = ""

    @Column()
    var password = ""
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }


    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email')"
    }


}