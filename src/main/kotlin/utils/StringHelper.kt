package utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.db.MysqlDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import kotlin.String
import java.security.NoSuchAlgorithmException
import io.jsonwebtoken.CompressionCodecs
import user.UserDb
import io.jsonwebtoken.Jwts
import java.util.*
import io.jsonwebtoken.SignatureAlgorithm


fun String.jsonToMap(): HashMap<String, Any> {
    val mapper = ObjectMapper()
    val typeRef = object : TypeReference<HashMap<String, Any>>() {

    }
    val o = mapper.readValue<HashMap<String, Any>>(this, typeRef)

    return o
}

fun String.stringToMD5(s: String): String {
    val MD5 = "MD5"
    try {
        // Create MD5 Hash
        val digest = java.security.MessageDigest
                .getInstance(MD5)
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()

        // Create Hex String
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2)
                h = "0" + h
            hexString.append(h)
        }
        return hexString.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}

fun Boolean.getToken(s: String): Boolean {
    //database type
    val databaseType = MysqlDatabaseType()
    //connect to database
    val connectionSource = JdbcConnectionSource("jdbc:postgresql://ec2-54-227-252-202.compute-1.amazonaws.com:5432/d879hf7441jppf", databaseType)
    connectionSource.setUsername("tzzjqsampsxhgx")
    connectionSource.setPassword("41f9cb24177a90ef15d4849da74c50453d1a29b6901be2dcc1794cb7cd3d0768")
    val daoUser = DaoManager.createDao(connectionSource, UserDb::class.java) as Dao<UserDb, String>
    var validToken: Boolean
    val queryBuilder = daoUser.queryBuilder()
    queryBuilder.where().eq("token", s)
    val userByToken = daoUser.query(queryBuilder.prepare())
    validToken = userByToken.isNotEmpty()
//    for (i in allUsers.indices) {
//        validToken = allUsers[i].token == s
//        if (validToken) {
//            break
//        }
//
////        println(userToken)
////        println("input user$s" )
//
//    }
    println(validToken)

    return validToken
}
fun Boolean.getTokenBoll(s: String): Boolean {
    //database type
    val databaseType = MysqlDatabaseType()
    //connect to database
    val connectionSource = JdbcConnectionSource("jdbc:mysql://localhost:3306/kajian", "root", "", databaseType)
    val daoUser = DaoManager.createDao(connectionSource, UserDb::class.java) as Dao<UserDb, String>
    var validToken: Boolean
    val allUsers = daoUser.queryForAll()
    validToken = allUsers.isNotEmpty()
    for (i in allUsers.indices) {
        validToken = allUsers[i].token == s
        if (validToken) {
            break
        }

//        println(userToken)
//        println("input user$s" )

    }
    println(validToken)

    return validToken
}


//Create token
 fun String.createToken(subject:String,key :String): String {
    val compactJws = Jwts.builder()
            .setSubject(subject)
            .compressWith(CompressionCodecs.DEFLATE)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact()


   return compactJws
}

