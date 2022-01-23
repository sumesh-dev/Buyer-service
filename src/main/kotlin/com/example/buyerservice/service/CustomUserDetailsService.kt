package com.example.buyerservice.service

import com.example.buyerservice.dao.IUserRepository
import com.example.buyerservice.models.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var iUserRepository: IUserRepository



    override fun loadUserByUsername(email: String): UserDetails {
        val user: UserDao = iUserRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with username: $email")
        val list = mutableListOf(SimpleGrantedAuthority(user.role))
        return User(user.email, user.password,list)
    }

}