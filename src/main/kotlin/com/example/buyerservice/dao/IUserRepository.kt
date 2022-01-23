package com.example.buyerservice.dao

import com.example.buyerservice.models.UserDao
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface IUserRepository: MongoRepository<UserDao,ObjectId>{

    @Query("{email:?0}")
    fun findByEmail(email: String?): UserDao?
//    @Query("db.users.remove({email:?0})")
//    fun deleteByEmail(email: String)
    @Query("{role:ROLE_seller}")
    fun findAllSeller(): MutableList<UserDao>?

    @Query("{role:ROLE_customer}")
    fun findAllCustomer(): MutableList<UserDao>?

}