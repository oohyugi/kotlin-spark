import spark.Spark.*


object Main {
    @JvmStatic fun main(args: Array<String>) {
        port(getHerokuAssignedPort())
        Router().run()
    }

    fun getHerokuAssignedPort(): Int {
        val processBuilder = ProcessBuilder()
        if (processBuilder.environment()["PORT"] != null) {
            return Integer.parseInt(processBuilder.environment()["PORT"])
        }
        return 4567 //return default port if heroku-port isn't set (i.e. on localhost)
    }
}