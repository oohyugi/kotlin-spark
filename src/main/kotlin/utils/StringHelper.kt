package utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.db.MysqlDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import java.util.HashMap
import kotlin.String
import java.security.NoSuchAlgorithmException
import org.eclipse.jetty.util.security.Credential.MD5.digest
import com.oracle.util.Checksums.update
import news.NewsDb
import user.UserDb
import java.security.MessageDigest


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
    val connectionSource = JdbcConnectionSource("jdbc:mysql://localhost:3306/kajian", "root", "", databaseType)
    val daoUser = DaoManager.createDao(connectionSource, UserDb::class.java) as Dao<UserDb, String>
    var validToken: Boolean = true
    val allUsers = daoUser.queryForAll()
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

