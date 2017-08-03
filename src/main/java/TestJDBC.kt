import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object TestJDBC {

    @JvmStatic fun main(argv: Array<String>) {

        println("-------- MySQL JDBC Connection Testing ------------")

        String
        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            println("Where is your MySQL JDBC Driver?")
            e.printStackTrace()
            return
        }

        println("MySQL JDBC Driver Registered!")
        var connection: Connection? = null

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/sample", "root", "")

        } catch (e: SQLException) {
            println("Connection Failed! Check output console")
            e.printStackTrace()
            return
        }

        if (connection != null) {
            println("You made it, take control your database now!")
        } else {
            println("Failed to make connection!")
        }
    }
}


