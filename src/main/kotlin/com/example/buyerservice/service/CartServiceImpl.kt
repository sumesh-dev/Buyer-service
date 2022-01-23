package com.example.buyerservice.service

import com.example.buyerservice.client.ProductClient
import com.example.buyerservice.dao.IUserRepository
import com.example.buyerservice.models.Product
import com.example.buyerservice.models.UserDao
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class CartServiceImpl: CartService {

    @Autowired
    lateinit var userRepository: IUserRepository

    @Autowired
    lateinit var productClient: ProductClient

    @Autowired
    private val restTemplate: RestTemplate? = null

    @Value("\${Feign.Product_url}")
    lateinit var productUrl:URI

    @Value("\${jwt.secret}")
    private val secret: String? = null

    override fun addToCart(product_id: ObjectId, email: String): String {
        val userDao: UserDao? = userRepository.findByEmail(email)
        return if (userDao != null) {
            userDao.productInCart?.add(product_id)
            userRepository.save(userDao)
            "Product successfully added to cart"
        } else {
            "error occurred"
        }
    }

    override fun deleteToCart(product_id: ObjectId, email: String): String {
        val userDao: UserDao? = userRepository.findByEmail(email)
        return if (userDao != null) {
            userDao.productInCart?.remove(product_id)
//            userRepository.deleteById(userDao._id)
            userRepository.save(userDao)
            "Product successfully deleted from cart"
        } else {
            "error occurred"
        }

    }

    override fun showAllItemsInCart(email: String): MutableList<Product>? {
        val list: MutableList<Product> = mutableListOf<Product>()
        val cartProduct: MutableList<ObjectId>? = userRepository.findByEmail(email)?.productInCart
        if (cartProduct != null) {
            for (product_id in cartProduct) {
                var product = productClient.getProduct(productUrl,product_id.toString())
                    list.add(product)
            }
//            println(list)
//            println(list.toString())
        }
        return list
    }

    override fun showProductIdInCart(email: String):MutableList<String>? {
        val productInCart = mutableListOf<String>()
        userRepository.findByEmail(email)?.productInCart?.forEach {
            productInCart.add(it.toString())
        }
        return productInCart
    }

    override fun deleteAllProductFromCart(email: String): String {
        val userDao: UserDao? = userRepository.findByEmail(email)
            return if (userDao != null) {
                userDao.productInCart?.forEach { deleteToCart(it, email) }
                 "products deleted"
            }
            else {
                "error occurred"
            }
    }
}