package com.example.buyerservice.models

import java.time.LocalDateTime
import javax.validation.constraints.Max

data class Review(
    var firstName:String?,
    var email:String,
    @field:Max(5)
    var rating:Int,
    var commentDate:String =LocalDateTime.now().toString(),
    var comment:String?
)
