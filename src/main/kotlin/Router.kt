import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import spark.Spark.*
import java.util.*
import com.j256.ormlite.db.MysqlDatabaseType
import com.sun.tools.jdi.IntegerValueImpl
import news.NewsDb
import utils.baseResponse
import utils.jsonToMap
import user.UserDb
import utils.getToken
import utils.stringToMD5
import java.util.ArrayList




class Router {
    fun run() {

        //database type
        val databaseType = MysqlDatabaseType()


        //connect to database
        val connectionSource = JdbcConnectionSource("jdbc:mysql://localhost:3306/kajian", "root", "", databaseType)
        val dao = DaoManager.createDao(connectionSource, NewsDb::class.java) as Dao<NewsDb, kotlin.String>
        // create table
        TableUtils.createTableIfNotExists(connectionSource, NewsDb::class.java)

        val daoUser = DaoManager.createDao(connectionSource, UserDb::class.java) as Dao<UserDb, kotlin.String>
        // create table
        TableUtils.createTableIfNotExists(connectionSource, UserDb::class.java)


        notFound { req, res ->
            res.type("application/json")
            "{\"message\":\"404\"}"
        }

        internalServerError { req, res ->
            res.type("application/json")
            "{\"message\":\"Error 500\"}"
        }

//        //get all token
//        val allUsers = daoUser.queryForAll()
//        var userToken :String = ""
//        for (i in allUsers.indices) {
//
//            userToken = allUsers[i].token
//        }

        path("/news") {


            //all
            get("") { request, response ->

                val token = request.headers("userToken")
                val validToken : Boolean = true

                if (validToken.getToken(token)){
                    val news = dao.queryForAll()
                    response.baseResponse(200, "success", news)
                }else{
                    response.baseResponse(400, "Wrong Token", "")
                }



            }

            //add data
            post("/add") { request, response ->
                //                save data
                val newsDB = NewsDb(title = request.body().jsonToMap().get("title") as String, description = request.body().jsonToMap().get("description") as String)
                dao.create(newsDB)
                response.baseResponse(200, "OK", newsDB)


            }
            post("/update/:id"){request, response ->
                if (dao.idExists(request.params("id"))){
                    val newsDB = NewsDb(id = request.params("id").toInt(),title = request.body().jsonToMap().get("title") as String, description = request.body().jsonToMap().get("description") as String)
                    dao.update(newsDB)
                    response.baseResponse(200,"Update success",newsDB)
                }else{
                    response.baseResponse(201,"Data not found","")
                }
            }

            //remove data by id
            delete("/:id") { request, response ->
                if (dao.idExists(request.params("id"))) {

                    dao.deleteById(request.params("id"))
                    response.baseResponse(200, "Data berhasil dihapus", "")
                } else {
                    response.baseResponse(201, "Data not found", "")
                }

            }



            // pagination
            get("/page/:page") { request, response ->

                val queryBuilder = dao.queryBuilder()
                queryBuilder.offset((request.params("page").toLong() - 1) * (5)).limit(5)
                val news = dao.query(queryBuilder.prepare())
                response.baseResponse(200, "success", news)


            }

            //get data by id
            get("/:id") { request, response ->

                if (dao.idExists(request.params("id"))) {
                    val news = dao.queryForId(request.params("id"))
                    response.baseResponse(200, "success", news)
                } else {
                    response.baseResponse(200, "Data not found", "")
                }


            }




        }

        path("/user"){
            post("/add") { request, response ->
                val timestamp: Date = Date()
                val name = request.body().jsonToMap().get("name")
                val  token = timestamp.toString() + name
                val tokenMd5 = String().stringToMD5(token)
                val users = UserDb(name = request.body().jsonToMap().get("name") as String,
                        email = request.body().jsonToMap().get("email") as String,
                        token =tokenMd5 )
                daoUser.create(users)
                response.baseResponse(200, "Data not found", users)

            }
        }




    }


}