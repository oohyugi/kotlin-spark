package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response

//Extention manupulasi / menambah satu method pad response yaitu baseResponse
fun Response.baseResponse(code : Int,message :String,data: Any) : String {
    this.header("content-type","application/json")
    val mData = BaseResponse(code,message,data)
    return jacksonObjectMapper().writeValueAsString(mData)
}

data class BaseResponse(val code : Int, val message :String, val data: Any)