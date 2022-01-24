package com.example.buyerservice

import com.example.buyerservice.dao.IUserRepository
import com.example.buyerservice.models.UserDao
import com.example.buyerservice.models.UserDaoResponse
import com.example.buyerservice.service.IUserService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class BuyerServiceApplicationTests {

//	@Test
//	fun contextLoads() {
//	}

	@Autowired
	private lateinit var iUserService: IUserService

	@MockBean
	private lateinit var userRepository: IUserRepository


	@Test
	fun addUserTest(){
		val user:UserDao = UserDao("test","account","test@gmail.com","test123","buyer",null)
		Mockito.`when`(userRepository.save(user)).thenReturn(user)
		Mockito.`when`(userRepository.findByEmail(user.email)).thenReturn(user)
		Assertions.assertEquals("user already have account with this mail id ",iUserService.addUser(user))
	}

	@Test
	fun getUserByEmailTest(){
		val user:UserDao = UserDao(ObjectId("61ee597e731a3b6965939a02"),"test","account","test@gmail.com","test123","buyer",null)
		Mockito.`when`(userRepository.save(user)).thenReturn(user)
		Mockito.`when`(userRepository.findByEmail(user.email)).thenReturn(user)
		Assertions.assertEquals(UserDaoResponse(user._id.toString(),user.firstName,user.lastName,user.email,user.role.substringAfter("_"),
			user.productInCart.map { it.toString() } as MutableList<String>),iUserService.getUserByEmail(user.email))
	}

	@Test
	fun updateUserTest(){
		val user:UserDao = UserDao(ObjectId("61ee597e731a3b6965939a02"),"test","account","test@gmail.com","test123","buyer",null)
		val userUpdate:UserDao = UserDao(ObjectId("61ee597e731a3b6965939a02"),"testuser","account","test1@gmail.com","test123","buyer",null)
		Mockito.`when`(userRepository.save(user)).thenReturn(user)
		Mockito.`when`(userRepository.findByEmail(user.email)).thenReturn(user)
		Assertions.assertEquals("you cannot change email or role",iUserService.updateUserByEmail(user.email,userUpdate))
	}

	@Test
	fun deleteUserTest(){
		val user:UserDao = UserDao(ObjectId("61ee597e731a3b6965939a02"),"test","account","test@gmail.com","test123","buyer",null)
		Mockito.`when`(userRepository.save(user)).thenReturn(user)
		Mockito.`when`(userRepository.findByEmail(user.email)).thenReturn(user)
		Assertions.assertEquals("user deleted successfully",iUserService.deleteUserByEmail(user.email))
	}

}
