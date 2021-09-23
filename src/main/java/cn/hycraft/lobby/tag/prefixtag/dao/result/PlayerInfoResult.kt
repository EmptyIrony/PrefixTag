package cn.hycraft.lobby.tag.prefixtag.dao.result

import org.apache.ibatis.annotations.AutomapConstructor

data class PlayerInfoResult @AutomapConstructor constructor(var uuid: String) {
    var currentTag: String? = null
    var unlockedTag: String = ""
}