package cn.hycraft.lobby.tag.prefixtag.data

import org.bukkit.Material

data class Tag(val key: String) {
    companion object {
        val cache = HashMap<String, Tag>()
    }

    lateinit var displayName: String

    lateinit var realDisplay: String

    lateinit var icon: Material

    val descriptions = ArrayList<String>()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }


}