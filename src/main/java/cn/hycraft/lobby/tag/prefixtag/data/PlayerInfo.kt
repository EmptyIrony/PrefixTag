package cn.hycraft.lobby.tag.prefixtag.data

import cn.hycraft.lobby.tag.prefixtag.dao.result.PlayerInfoResult
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

data class PlayerInfo(val uuid: UUID) {
    companion object {
        val cache = HashMap<UUID, PlayerInfo>()
    }

    var currentTag: Tag? = null

    val unlockedTag = HashSet<Tag>()

    fun loadFromResult(result: PlayerInfoResult) {
        if (result.currentTag != null) {
            this.currentTag = Tag.cache[result.currentTag]
        }
        val split = result.unlockedTag.split(",")

        unlockedTag.clear()

        for (key in split) {
            val tag = Tag.cache[key] ?: continue
            unlockedTag.add(tag)
        }
    }

    fun saveToResult(): PlayerInfoResult {
        return PlayerInfoResult(this.uuid.toString()).also {
            it.currentTag = currentTag?.key

            if (unlockedTag.isNotEmpty()) {
                val sb = StringBuilder()
                for (tag in unlockedTag) {
                    sb.append(tag.key)
                        .append(",")
                }
                it.unlockedTag = sb.toString()
            }
        }
    }

}