
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import spark.Spark.*
import com.j256.ormlite.db.MysqlDatabaseType
import com.j256.ormlite.db.PostgresDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import news.NewsDb
import user.UserDb
import utils.*
import spark.Request
import spark.Response
import java.util.*


class Router {
    fun run() {

        //database type
        val databaseType = PostgresDatabaseType()


        val userDao = UserDb()
        //connect to database

        val connectionSource = JdbcConnectionSource("jdbc:postgresql://ec2-54-227-252-202.compute-1.amazonaws.com:5432/d879hf7441jppf", databaseType)
            connectionSource.setUsername("tzzjqsampsxhgx")
        connectionSource.setPassword("41f9cb24177a90ef15d4849da74c50453d1a29b6901be2dcc1794cb7cd3d0768")
        val dao = DaoManager.createDao(connectionSource, NewsDb::class.java) as Dao<NewsDb, String>
        val daoUser = DaoManager.createDao(connectionSource, UserDb::class.java) as Dao<UserDb, kotlin.String>
        // create table

       if (!dao.isTableExists){
           TableUtils.createTableIfNotExists(connectionSource, NewsDb::class.java)
       }
        if (!daoUser.isTableExists){
            // create table
            TableUtils.createTableIfNotExists(connectionSource, UserDb::class.java)
        }





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
                val validToken: Boolean = true

                if (validToken.getToken(token)) {
                    val news = dao.queryForAll()
                    response.baseResponse(200, "success", news)
                } else {
                    response.baseResponse(400, "Invalid Token", "")
                }


            }

            //add data
            post("/add") { request, response ->
                //                save data
                val title = request.body().jsonToMap().get("title")
                val desc = request.body().jsonToMap().get("description")
                val waktu = request.body().jsonToMap().get("waktu")
                val imgUrl = request.body().jsonToMap().get("img_url")
                val pemateri = request.body().jsonToMap().get("pemateri")
                val img_pemateri_url = request.body().jsonToMap().get("img_Pemateri_url")
                val tempat = request.body().jsonToMap().get("tempat")
                val cp = request.body().jsonToMap().get("cp")
                val newsDB = NewsDb(
                        title = title.toString(),
                        description = desc.toString(),
                        waktu = waktu.toString(),
                        img_url = imgUrl.toString(),
                        pemeteri = pemateri.toString(),
                        img_pemateri_url = img_pemateri_url.toString(),
                        tempat = tempat.toString(),
                        cp = cp.toString()
                )
                dao.create(newsDB)
                response.baseResponse(200, "Data berhasil ditambahkan", newsDB)


            }
            post("/update/:id") { request, response ->
                val token = request.headers("userToken")
                val id = request.params("id")
                val title = request.body().jsonToMap().get("title")
                val desc = request.body().jsonToMap().get("description")
                val waktu = request.body().jsonToMap().get("waktu")
                val imgUrl = request.body().jsonToMap().get("img_url")
                val pemateri = request.body().jsonToMap().get("pemateri")
                val img_pemateri_url = request.body().jsonToMap().get("img_Pemateri_url")
                val tempat = request.body().jsonToMap().get("tempat")
                val cp = request.body().jsonToMap().get("cp")
                val validToken: Boolean = true
                if (validToken.getToken(token)) {

                } else {

                }
                if (dao.idExists(id)) {
                    val newsDB = NewsDb(
                            id = id.toInt(),
                            title = title.toString(),
                            description = desc.toString(),
                            waktu = waktu.toString(),
                            img_url = imgUrl.toString(),
                            pemeteri = pemateri.toString(),
                            img_pemateri_url = img_pemateri_url.toString(),
                            tempat = tempat.toString(),
                            cp = cp.toString()
                    )
                    dao.update(newsDB)
                    response.baseResponse(200, "Update success", newsDB)
                } else {
                    response.baseResponse(201, "Data not found", "")
                }
            }

            //remove data by id
            delete("/:id") { request, response ->

                val token = request.headers("userToken")
                val validToken: Boolean = true
                if (validToken.getToken(token)) {
                    if (dao.idExists(request.params("id"))) {

                        dao.deleteById(request.params("id"))
                        response.baseResponse(200, "Data berhasil dihapus", "")
                    } else {
                        response.baseResponse(201, "Data not found", "")
                    }
                } else {

                    response.baseResponse(201, "Invalid Token", "")
                }


            }


            // pagination
            get("/page/:page") { request, response ->
                val token = request.headers("userToken")
                val validToken: Boolean = true
                if (validToken.getToken(token)) {
                    val queryBuilder = dao.queryBuilder()
                    queryBuilder.offset((request.params("page").toLong() - 1) * (5)).limit(5)
                    val news = dao.query(queryBuilder.prepare())
                    response.baseResponse(200, "success", news)
                } else {
                    response.baseResponse(200, "Invalid Token", "")
                }


            }

            //get data by id
            get("/:id") { request, response ->
                val token = request.headers("userToken")
                val validToken: Boolean = true
                if (validToken.getToken(token)) {
                    if (dao.idExists(request.params("id"))) {
                        val news = dao.queryForId(request.params("id"))
                        response.baseResponse(200, "success", news)
                    } else {
                        response.baseResponse(200, "Data not found", "")
                    }
                } else {
                    response.baseResponse(200, "Invalid Token", "")
                }


            }


        }

        path("/user") {
            post("/register") { request, response ->

                val timestamp: Date = Date()
                val name = request.body().jsonToMap().get("name")
                val email = request.body().jsonToMap().get("email")
                val password = request.body().jsonToMap().get("password")
                val telpon = request.body().jsonToMap().get("telpon")
                val urlPict = request.body().jsonToMap().get("url_picture")
                val passHash = String().stringToMD5(password.toString())
                val token = String().createToken(timestamp.toString(), name.toString())

                val users = UserDb(name = name.toString(),
                        email = email.toString(),
                        password = passHash,
                        telpon = telpon.toString(),
                        url_picture = urlPict.toString(),
                        token = token)
                daoUser.create(users)
                response.baseResponse(200, "Success", users)


            }
            post("/login") { request, response ->
                val email = request.body().jsonToMap().get("email")
                val password = request.body().jsonToMap().get("password")
                val passHash = String().stringToMD5(password.toString())
                val queryBuilder = daoUser.queryBuilder()
                queryBuilder.where().eq("email", email).and().eq("password", passHash)
                val userLogin = daoUser.query(queryBuilder.prepare())
                val isSuccessLogin: Boolean
                isSuccessLogin = userLogin.isNotEmpty()
                if (isSuccessLogin) {
                    response.baseResponse(200, "Login Success", userLogin)
                } else {
                    response.baseResponse(201, "Login Failed", "")
                }


            }
            post("/update/:id") { request, response ->
                val token = request.headers("userToken")
                val id = request.params("id")
                val validToken: Boolean = true
                val name = request.body().jsonToMap().get("name")
                val email = request.body().jsonToMap().get("email")
                val telpon = request.body().jsonToMap().get("telpon")
                val urlPict = request.body().jsonToMap().get("url_picture")
                val users = UserDb(id = id.toInt(), name = name.toString(),
                        email = email.toString(),
                        telpon = telpon.toString(),
                        url_picture = urlPict.toString()
                )

                response.baseResponse(200, "Success", users)
                if (validToken.getToken(token)) {
                    if (daoUser.idExists(id)) {
                        val updateBuilder = daoUser.updateBuilder()
                        updateBuilder.where().eq("id", id)
                        updateBuilder.updateColumnValue("name" /* column */, name /* value */)
                        updateBuilder.updateColumnValue("email" /* column */, email /* value */)
                        updateBuilder.updateColumnValue("telpon" /* column */, telpon /* value */)
                        updateBuilder.updateColumnValue("url_picture" /* column */, urlPict /* value */)
                        updateBuilder.update()
                        response.baseResponse(200, "Update success", users)
                    } else {
                        response.baseResponse(201, "Data not found", "")
                    }
                } else {
                    response.baseResponse(201, "Invalid Token", "")
                }


            }

            post("/forgotpassword/:id") { request, response ->
                val token = request.headers("userToken")
                val validToken: Boolean = true
                if (validToken.getToken(token)) {
                    val id = request.params("id")
                    val oldPassword = request.body().jsonToMap().get("old_password")
                    val newPassword = request.body().jsonToMap().get("new_password")
                    val passwordConfirm = request.body().jsonToMap().get("password_confirm")
                    val oldPassHash = String().stringToMD5(oldPassword.toString())
                    val newPassHash = String().stringToMD5(newPassword.toString())
                    val passHashConfirm = String().stringToMD5(passwordConfirm.toString())
                    val queryBuilder = daoUser.queryBuilder()
                    queryBuilder.where().eq("password", oldPassHash).and().eq("id", id)
                    val userLogin = daoUser.query(queryBuilder.prepare())
                    if (userLogin.isNotEmpty()) {

                        if (newPassHash.equals(passHashConfirm)) {

                            if (!newPassHash.equals(oldPassHash) && !passHashConfirm.equals(oldPassHash)) {
                                val updateBuilder = daoUser.updateBuilder()
                                updateBuilder.where().eq("id", id)
                                updateBuilder.updateColumnValue("password" /* column */, passHashConfirm /* value */)
                                updateBuilder.update()
                                val userUpdate = daoUser.update(updateBuilder.prepare())
                                response.baseResponse(200, "Change Password Success", userUpdate)
                            } else {
                                response.baseResponse(201, "Password baru tidak boleh sama dengan yg lama", "")
                            }


                        } else {
                            response.baseResponse(201, "Password not matching", "")
                        }
                    } else {
                        response.baseResponse(201, "Old Password Wrong", "")
                    }


                } else {
                    response.baseResponse(201, "Invalid Token", "")
                }


            }


        }


    }


}