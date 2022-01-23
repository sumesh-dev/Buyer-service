package com.example.buyerservice.models

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Document("Users")
class UserDao{

    @Autowired
    private var bCryptPasswordEncoder = BCryptPasswordEncoder(10)

    @Id
    lateinit var _id: ObjectId

    @field:NotBlank(message = "first name is mandatory")
    lateinit var firstName:String

    @field:NotBlank(message = "last name is mandatory")
    lateinit var lastName:String

    @field:Email(message = "Email id is mandatory")
    @field:NotBlank(message = "email is mandatory")
//    @Indexed(unique=true)
     lateinit var email:String

    @field:NotBlank(message = "password is mandatory")
    lateinit var password:String

    @field:NotBlank(message = "Role is mandatory")
    lateinit var role:String

    var productInCart: MutableList<ObjectId> = mutableListOf<ObjectId>()

    constructor(
        _id: ObjectId, firstName: String, lastName: String,email: String, password: String, role: String, productInCart:ObjectId?)
    {
        this._id = _id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        if (password.length > 30){
            this.password = password
        }
        else {
            this.password = this.bCryptPasswordEncoder.encode(password)
        }
        if(!role.startsWith("ROLE")){
            this.role = "ROLE_"+role.lowercase()
        }
        else{
            this.role = role.lowercase()
        }
        if (productInCart != null) {
            this.productInCart.add(productInCart)
        }
    }

    constructor(
        firstName: String,lastName: String,email: String, password: String,role: String, productInCart:ObjectId?)
    {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        if (password.length > 30){
            this.password = password
        }
        else {
            this.password = this.bCryptPasswordEncoder.encode(password)
        }
        if(!role.startsWith("ROLE")){
            this.role = "ROLE_"+role.lowercase()
        }
        else{
            this.role = role.lowercase()
        }
        if (productInCart != null) {
            this.productInCart.add(productInCart)
        }
    }

    constructor()

    override fun toString(): String {
        return "UserDao(firstName='$firstName', lastName='$lastName', email='$email', password='$password', role='$role', productInCart=$productInCart)"
    }


}

