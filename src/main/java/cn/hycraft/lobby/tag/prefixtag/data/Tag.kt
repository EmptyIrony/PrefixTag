package cn.hycraft.lobby.tag.prefixtag.data

data class Tag(val key: String) {
    companion object {
        val cache = HashMap<String, Tag>()
    }

    lateinit var displayName: String

    lateinit var realDisplay: String

    var claimStartTime = 0L
    var claimEndTime = 0L

    val descriptions = ArrayList<String>()

}