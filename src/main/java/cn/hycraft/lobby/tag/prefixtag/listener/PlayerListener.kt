package cn.hycraft.lobby.tag.prefixtag.listener

import cn.hycraft.lobby.tag.prefixtag.PrefixTag
import cn.hycraft.lobby.tag.prefixtag.event.TagLoadedEvent
import cn.hycraft.lobby.tag.prefixtag.refreshTag
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerListener: Listener{

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        PrefixTag.getPlayerService().loadPlayerInfo(event.player.uniqueId)

        Bukkit.getScheduler().scheduleSyncDelayedTask(PrefixTag.INSTANCE) {
            for (entry in PrefixTag.cachedEntity.entries) {
                val player = Bukkit.getPlayer(entry.key) ?: continue
                val armorStand = entry.value

                val spawnPacket = PacketPlayOutSpawnEntityLiving(armorStand)
                val metaPacket = PacketPlayOutEntityMetadata(armorStand.id, armorStand.dataWatcher, false)
                val attachPacket = PacketPlayOutAttachEntity(0, armorStand, (player as CraftPlayer).handle)

                val connection = (event.player as CraftPlayer).handle.playerConnection
                connection.sendPacket(spawnPacket)
                connection.sendPacket(metaPacket)
                connection.sendPacket(attachPacket)
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        val armorStand = PrefixTag.cachedEntity.remove(player.uniqueId) ?: return
        val destroyPacket = PacketPlayOutEntityDestroy(armorStand.id)

        for (target in Bukkit.getOnlinePlayers()) {
            val entityPlayer = (target as CraftPlayer).handle
            entityPlayer.playerConnection.sendPacket(destroyPacket)
        }
    }

    @EventHandler
    fun onLoaded(event: TagLoadedEvent) {
        event.player.refreshTag()
    }

}