package cn.hycraft.lobby.tag.prefixtag.dao.mapper

import cn.hycraft.lobby.tag.prefixtag.dao.result.PlayerInfoResult

interface PlayerInfoMapper {

    fun selectPlayerByUuid(uuid: String): PlayerInfoResult?

    fun updatePlayerWear(result: PlayerInfoResult)

    fun updatePlayerUnlock(result: PlayerInfoResult)

    fun insertPlayerData(result: PlayerInfoResult)

}