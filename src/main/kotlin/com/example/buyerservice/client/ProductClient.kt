package com.example.buyerservice.client

import com.example.buyerservice.models.Product
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.net.URI


//@FeignClient(url="http://localhost:9003/product", name = "PRODUCT-CLIENT")
@FeignClient(url="http://inventory-service.eba-2kbtwhiu.us-east-1.elasticbeanstalk.com/product",name = "PRODUCT-CLIENT")
interface ProductClient {

    @GetMapping("/getProduct/{id}")
    fun getProduct(@PathVariable(name = "id") id : String) :Product

}