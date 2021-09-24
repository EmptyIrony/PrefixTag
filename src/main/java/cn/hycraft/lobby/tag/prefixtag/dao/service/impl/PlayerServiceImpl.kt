package cn.hycraft.lobby.tag.prefixtag.dao.service.impl

import cn.hycraft.lobby.tag.prefixtag.dao.factory.HikariDataSourceFactory
import cn.hycraft.lobby.tag.prefixtag.dao.mapper.PlayerInfoMapper
import cn.hycraft.lobby.tag.prefixtag.dao.service.PlayerService
import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import cn.hycraft.lobby.tag.prefixtag.event.TagLoadedEvent
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.Executors

class PlayerServiceImpl: PlayerService{
    private val service = Executors.newScheduledThreadPool(2)

    override fun loadPlayerInfo(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return

        service.submit {
            HikariDataSourceFactory.getSqlSessionFactory().openSession(true).use { session ->
                val mapper = session.getMapper(PlayerInfoMapper::class.java)
                val result = mapper.selectPlayerByUuid(uuid.toString())
                if (result != null) {
                    val info = PlayerInfo(uuid).also {
                        it.loadFromResult(result)
                    }
                    PlayerInfo.cache[uuid] = info

                    TagLoadedEvent(player).callEvent()

                    return@submit
                }

                val info = PlayerInfo(uuid)
                mapper.insertPlayerData(info.saveToResult())

                PlayerInfo.cache[uuid] = info

                TagLoadedEvent(player).callEvent()

            }
        }
    }

    override fun savePlayerInfoWear(uuid: UUID) {
        val info = PlayerInfo.cache[uuid] ?: return
        service.submit {
            HikariDataSourceFactory.getSqlSessionFactory().openSession(true).use { session ->
                val mapper = session.getMapper(PlayerInfoMapper::class.java)
                mapper.updatePlayerWear(info.saveToResult())
            }
        }
    }

    override fun savePlayerInfoUnlock(uuid: UUID) {
        val info = PlayerInfo.cache[uuid] ?: return
        service.submit {
            HikariDataSourceFactory.getSqlSessionFactory().openSession(true).use { session ->
                val mapper = session.getMapper(PlayerInfoMapper::class.java)
                mapper.updatePlayerUnlock(info.saveToResult())
            }
        }
    }
}