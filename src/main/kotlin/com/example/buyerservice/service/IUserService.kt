package com.example.buyerservice.service

import com.example.buyerservice.models.UserDao
import org.bson.types.ObjectId

interface IUserService {
    fun addUser(userDao: UserDao):String?
    fun getUserByEmail(email: String):Any?
    fun deleteUserByEmail(email: String):String
    fun updateUserByEmail(email:String, userDao: UserDao):String
}