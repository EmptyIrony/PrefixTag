package cn.hycraft.lobby.tag.prefixtag.dao.service

import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import java.util.*

interface PlayerService {

    fun loadPlayerInfo(uuid: UUID)

    fun savePlayerInfoWear(uuid: UUID)

    fun savePlayerInfoUnlock(uuid: UUID)

}