package com.example.buyerservice.config

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter(){

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null

    @Value("\${allowed.origin}")
    lateinit var origin: String

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
//        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(httpSecurity: HttpSecurity) {

        httpSecurity
            .csrf().ignoringAntMatchers("/cart/showProductIDinCart/*").disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers("/cart/showProductIDinCart/**/**","/cart/deleteAllProductFromCart/**/**","/users/signup","/users/login").permitAll()
            .antMatchers("/users/getAllCustomer","/users/getAllSeller").hasRole("admin")
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

        httpSecurity
            .exceptionHandling()
            .accessDeniedHandler{ request, response, e ->
                println(e)
                response.contentType = "application/json;charset=UTF-8"
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer.write(
                    JSONObject()
                        .put("timestamp", LocalDateTime.now())
                        .put("message", "Access denied")
                        .toString()
                )
            }
            .authenticationEntryPoint { request, response, e ->
                response.contentType = "application/json;charset=UTF-8"
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer.write(
                    JSONObject()
                        .put("timestamp", LocalDateTime.now())
                        .put("message", "Access denied")
                        .toString()
                )
            }


//        httpSecurity.csrf().ignoringAntMatchers()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf(origin)
        configuration.allowedMethods = mutableListOf("GET", "POST","DELETE","PATCH")
        configuration.allowedHeaders = mutableListOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}