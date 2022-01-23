package com.example.buyerservice.service

import com.example.buyerservice.dao.IUserRepository
import com.example.buyerservice.models.UserDao
import com.example.buyerservice.models.UserDaoResponse
import org.bson.types.ObjectId
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : IUserService {

    @Autowired
    private lateinit var userRepository: IUserRepository

    override fun addUser(userDao: UserDao): String? {
        return if (userRepository.findByEmail(userDao.email)==null) {
            userRepository.save(UserDao(userDao.firstName,userDao.lastName,userDao.email,userDao.password,userDao.role,null))
            "user created successfully"
        } else
            "user already have account with this mail id "
    }

    override fun getUserByEmail(email: String): Any {
//        return userRepository.findByEmail(email) ?: "user does not exist"
        val userDao:UserDao? = userRepository.findByEmail(email)
        return if(userDao!=null){
            return UserDaoResponse(userDao._id.toString(),userDao.firstName,userDao.lastName,userDao.email,userDao.role.substringAfter("_"),
                userDao.productInCart.map { it.toString() } as MutableList<String>)
//            userDao._id = userDao._id.toString();
//            return """{"_id": "${userDao._id.toString()}","firstName": "${userDao.firstName}", "lastName":"${userDao.lastName}", "email":"${userDao.email}","role":"${userDao.role.substringAfter("_")}", "productInCart":"${userDao.productInCart?.forEach { \"+$it+\" }}"}""";
        }
        else{
            "user does not exist"
        }
    }

    override fun deleteUserByEmail(email: String): String {
        val user: UserDao? = userRepository.findByEmail(email)
        return if (user!==null) {
            userRepository.deleteById(user._id)
            "user deleted successfully"
        } else{
            "user does not exist"
        }
    }

    override fun updateUserByEmail(email: String, userDao: UserDao): String {
        val user: UserDao? = userRepository.findByEmail(email)
        return if (user!==null) {
    //            userRepository.deleteById(user._id)
//            println(userDao.toString())
//            println(user.toString())
            return if(userDao.email!=user.email||userDao.role!=user.role.replace("ROLE_","")){
                "you cannot change email or role"
            }
            else{
                userRepository.save(UserDao(user._id,userDao.firstName,userDao.lastName,userDao.email,userDao.password,userDao.role,null))
                "user updated successfully"
                }
            }
            else {
                "user does not exist"
            }
    }


}