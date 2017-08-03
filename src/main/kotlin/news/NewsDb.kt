package news

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*


@DatabaseTable(tableName = "news")
data class NewsDb (

    @DatabaseField(generatedId = true)
    var id: Int? = null,

    @DatabaseField(canBeNull = false)
    var title: String = "",

    @DatabaseField
    var description: String = "",

    @DatabaseField()
    var waktu: String = "",

    @DatabaseField
    var img_url: String = "",

    @DatabaseField
    var pemeteri: String = "",

    @DatabaseField
    var img_pemateri_url: String = "",

    @DatabaseField
    var tempat: String = "",

    @DatabaseField
    var cp: String = "",


    @DatabaseField
    var timestamp: Date = Date()
)