package com.example.buyerservice.config

import com.example.buyerservice.helper.JwtTokenUtil
import com.example.buyerservice.service.CustomUserDetailsService
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter : OncePerRequestFilter() {

    @Autowired
    private val customUserDetailsService: CustomUserDetailsService? = null

    @Autowired
    private lateinit var  jwtTokenUtil: JwtTokenUtil

        lateinit var email: String

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authorizationHeader:String? = request.getHeader("Authorization")

//        val requestTokenHeader: String? = request.getHeader("Authorization")
           var cookie: Cookie? = request.cookies?.find { c->c.name=="JwtToken" }
//            print("${cookie?.name} and ${cookie?.value}" )

        var username: String? = null
        var jwtToken: String? = cookie?.value
//        println(jwtToken)

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && jwtToken===null) {
            jwtToken = authorizationHeader.substring(7);
        }

        if (jwtToken != null) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken)
//                println("username from jwt token $username is $jwtToken")
                email = username
            }

            catch (e: IllegalArgumentException) {

                println("Unable to get JWT Token")
            }

            catch (e: ExpiredJwtException) {
                println("JWT Token has expired")
            }
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().authentication == null)
        {
            val userDetails: UserDetails? = customUserDetailsService?.loadUserByUsername(username)

            // if token is valid configure Spring Security to manually set
            // authentication


            if (userDetails.let { jwtTokenUtil.validateToken(jwtToken, it) }!!) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails?.authorities
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        chain.doFilter(request, response)
    }

}