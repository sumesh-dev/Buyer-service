package com.example.buyerservice.models

import java.io.Serializable
import javax.validation.constraints.Email

class JwtRequest : Serializable {
    @field:Email(message = "please enter valid email")
    var email: String = ""
    var password: String = ""

    //need default constructor for JSON Parsing
    constructor() {}
    constructor(username: String, password: String) {
        this.email = username
        this.password = password
    }

    companion object {
        private const val serialVersionUID = 5926468583005150707L
    }

    override fun toString(): String {
        return "JwtRequest(email='$email', password='$password')"
    }


}