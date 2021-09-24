package cn.hycraft.lobby.tag.prefixtag

import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.refreshTag() {
    val info = PlayerInfo.cache[this.uniqueId] ?: return
    val player = Bukkit.getPlayer(info.uuid) ?: return

    //移除原先显示的前缀
    val remove = PrefixTag.cachedEntity.remove(player.uniqueId)
    remove?.let {
        val destroyPacket = PacketPlayOutEntityDestroy(it.id)
        Bukkit.getOnlinePlayers().forEach { target ->
            (target as CraftPlayer).handle.playerConnection.sendPacket(destroyPacket)
        }
    }

    val tag = info.currentTag ?: return

    val armorStand = EntityArmorStand((player.world as CraftWorld).handle)

    armorStand.customName = tag.realDisplay
    armorStand.customNameVisible = true
    armorStand.isInvisible = true

    val spawnPacket = PacketPlayOutSpawnEntityLiving(armorStand)
    val metaPacket = PacketPlayOutEntityMetadata(armorStand.id, armorStand.dataWatcher, false)
    val attachPacket = PacketPlayOutAttachEntity(0, (player as CraftPlayer).handle, armorStand)

    for (target in Bukkit.getOnlinePlayers()) {
        val entityPlayer = (target as CraftPlayer).handle
        entityPlayer.playerConnection.sendPacket(spawnPacket)
        entityPlayer.playerConnection.sendPacket(metaPacket)
        entityPlayer.playerConnection.sendPacket(attachPacket)
    }

    PrefixTag.cachedEntity[player.uniqueId] = armorStand
}