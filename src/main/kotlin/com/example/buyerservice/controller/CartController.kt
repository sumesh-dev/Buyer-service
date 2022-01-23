package com.example.buyerservice.controller

import com.example.buyerservice.config.JwtRequestFilter
import com.example.buyerservice.models.Product
import com.example.buyerservice.service.CartService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart")
class CartController {

    @Autowired
    private lateinit var cartService: CartService

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @GetMapping("/{product_id}")
    fun addToCart(@PathVariable product_id:ObjectId):ResponseEntity<String> {
        return ResponseEntity<String>(jwtRequestFilter?.let { cartService.addToCart(product_id, it.email) }, HttpStatus.OK)
    }

    @DeleteMapping("/{product_id}")
    fun deleteToCart(@PathVariable product_id:ObjectId):ResponseEntity<String>{
       return ResponseEntity<String>(jwtRequestFilter?.let { cartService.deleteToCart(product_id, it.email) },HttpStatus.OK)
    }


    @GetMapping("/showAllItemInCart")
    fun showAllItemInCart():ResponseEntity<MutableList<Product>>?{
       return ResponseEntity<MutableList<Product>>(jwtRequestFilter?.let { cartService.showAllItemsInCart(it.email) },HttpStatus.OK)
    }

    @DeleteMapping("/deleteAllProductFromCart/{secretCode}/{email}")
    fun deleteAllProductFromCart(@PathVariable secretCode: String,@PathVariable email: String):ResponseEntity<Any>{
        return if (secretCode==secret) {
            ResponseEntity<Any>(cartService.deleteAllProductFromCart(email), HttpStatus.OK)
        }
        else {
            ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/showProductIDinCart/{secretCode}/{email}")
    fun showProductIdCart(@PathVariable secretCode: String,@PathVariable email: String):ResponseEntity<Any>?{
        return if (secretCode==secret) {
            ResponseEntity<Any>(cartService.showProductIdInCart(email), HttpStatus.OK)
        }
        else {
            ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        }
    }
}