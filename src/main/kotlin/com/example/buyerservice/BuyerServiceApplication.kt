package com.example.buyerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class BuyerServiceApplication

fun main(args: Array<String>) {
	runApplication<BuyerServiceApplication>(*args)
}
