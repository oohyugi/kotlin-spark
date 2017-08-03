package user

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "users")
data class UserDb(
        @DatabaseField(generatedId = true)
        var id: Int? = null,

        @DatabaseField(canBeNull = false)
        var name: String = "",

        @DatabaseField(canBeNull = false)
        var email: String = "",

        @DatabaseField(canBeNull = false)
        var password: String = "",

        @DatabaseField(canBeNull = false)
        var telpon: String = "",

        @DatabaseField(canBeNull = false)
        var url_picture: String = "",

        @DatabaseField(canBeNull = false)
        var token : String ="",

        @DatabaseField
        var timestamp: Date = Date()
)