package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response

//Extention manupulasi / menambah satu method pad response yaitu baseResponse
fun Response.baseResponse(code : Int,message :String,data: Any) : String {
    this.header("content-type","application/json")
    val baseResponse  = HashMap<String,Any>()
    baseResponse.put("status",code)
    baseResponse.put("message", message)
    baseResponse.put("data",data)

    return jacksonObjectMapper().writeValueAsString(baseResponse)
}
