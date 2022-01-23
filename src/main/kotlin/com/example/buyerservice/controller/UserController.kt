package com.example.buyerservice.controller

import com.example.buyerservice.config.JwtRequestFilter
import com.example.buyerservice.dao.IUserRepository
import com.example.buyerservice.helper.JwtTokenUtil
import com.example.buyerservice.models.JwtRequest
import com.example.buyerservice.models.UserDao
import com.example.buyerservice.service.CustomUserDetailsService
import com.example.buyerservice.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var iUserService: IUserService

    @Autowired
    private lateinit var iUserRepository: IUserRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null

    @Autowired
    private lateinit var  jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var customUserDetailsService: CustomUserDetailsService

    @PostMapping("/signup")
    fun addUser(@Valid @RequestBody userDao: UserDao): ResponseEntity<String> {
        return if(userDao.role.lowercase() == "admin" ||((userDao.role.lowercase()!="customer")&&(userDao.role.lowercase()!="seller"))){
            ResponseEntity<String>("Role can either customer or seller", HttpStatus.BAD_REQUEST)
        } else
            ResponseEntity<String>(iUserService.addUser(userDao), HttpStatus.OK)
    }

    @PostMapping("/login")
    @ResponseBody
    @Throws(Exception::class)
    fun createAuthenticationToken(@Valid @RequestBody jwtRequest: JwtRequest, response: HttpServletResponse): ResponseEntity<MutableMap<String,String>> {
        val data: MutableMap<String, String> = mutableMapOf()
        try{
            this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken(jwtRequest.email,jwtRequest.password))
        }
//        catch (e: UsernameNotFoundException,){
//            throw Exception("Invalid Credentials")
//        }
        catch(e: BadCredentialsException){
            data["msg"] = "Invalid Crendentials"
            return ResponseEntity<MutableMap<String,String>>(data,HttpStatus.BAD_REQUEST)
//            throw Exception("Invalid Credentials")
        }

        val userDetails: UserDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.email)
        val token: String = this.jwtTokenUtil.generateToken(userDetails)!!
        val cookie = Cookie("JwtToken",token)
        cookie.maxAge = 60*1000
//        cookie.isHttpOnly = true
//        cookie.secure = true
//        cookie.domain = "ecommerce-react-app.s3.ap-south-1.amazonaws.com"
        response.addCookie(cookie)
//        val headers = response.getHeaders(HttpHeaders.SET_COOKIE)
//        response.setHeader(HttpHeaders.SET_COOKIE,String.format("%s; %s", headers, "SameSite=NONE;Secure"))

        data["msg"] = "Login successful"
        data["JwtToken"] = token
        return ResponseEntity<MutableMap<String,String>>(data,HttpStatus.OK)
    }

    @GetMapping("/me")
    fun getUserByEmail(): ResponseEntity<Any> {
        return ResponseEntity<Any>(jwtRequestFilter?.email?.let { iUserService.getUserByEmail(it) }, HttpStatus.OK)
    }

    @DeleteMapping("/")
    fun deleteUserByEmail(): ResponseEntity<String> {
        return ResponseEntity<String>(jwtRequestFilter?.email?.let { iUserService.deleteUserByEmail(it) }, HttpStatus.OK)
    }

    @PatchMapping("/")
    fun updateUserByEmail(@Valid @RequestBody userDao: UserDao): ResponseEntity<String> {
        return ResponseEntity<String>(jwtRequestFilter?.email?.let { iUserService.updateUserByEmail(it,userDao) }, HttpStatus.OK)
    }

}