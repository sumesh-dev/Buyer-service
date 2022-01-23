package com.example.buyerservice.models

data class UserDaoResponse(var id:String,var firstName: String,var lastName:String,var email:String,var role:String,var productInCart:MutableList<String>)
