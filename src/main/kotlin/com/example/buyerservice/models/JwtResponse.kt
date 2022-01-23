package com.ecommerce.loginauthenticationservice.models

import java.io.Serializable

class JwtResponse : Serializable {

    companion object {
        private const val serialVersionUID = -8091879091924046844L
    }

     var jwttoken: String? = null

    constructor()

    constructor(jwttoken: String?) {
        this.jwttoken = jwttoken
    }




    //    fun JwtResponse(jwttoken: String?) {
//        this.jwttoken = jwttoken
//    }

//    @JvmName("getToken1")
//    fun getToken(): String? {
//        return jwttoken
//    }
}