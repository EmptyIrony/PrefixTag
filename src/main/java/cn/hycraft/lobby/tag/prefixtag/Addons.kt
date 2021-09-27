package cn.hycraft.lobby.tag.prefixtag

import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.refreshTag() {
    val info = PlayerInfo.cache[this.uniqueId] ?: return
    val player = Bukkit.getPlayer(info.uuid) ?: return
    if (!player.isOnline) {
        return
    }

    val vanish = this.isVanish()
    if (vanish) {
        return
    }

    //移除原先显示的前缀
    val remove = PrefixTag.cachedEntity.remove(player.uniqueId)
    remove?.let {
        val destroyPacket = PacketPlayOutEntityDestroy(it.id)
        Bukkit.getOnlinePlayers().forEach { target ->
            (target as CraftPlayer).handle.playerConnection.sendPacket(destroyPacket)
        }
    }

    val tag = info.currentTag ?: return

    val armorStand = EntityArmorStand((this.world as CraftWorld).handle)

    armorStand.customName = tag.realDisplay
    armorStand.customNameVisible = true
    armorStand.isInvisible = true
    armorStand.isSmall = true

    val spawnPacket = PacketPlayOutSpawnEntityLiving(armorStand)
    val metaPacket = PacketPlayOutEntityMetadata(armorStand.id, armorStand.dataWatcher, false)
    val attachPacket = PacketPlayOutAttachEntity(0, armorStand, (this as CraftPlayer).handle)

    for (target in Bukkit.getOnlinePlayers()) {
        val entityPlayer = (target as CraftPlayer).handle
        entityPlayer.playerConnection.sendPacket(spawnPacket)
        entityPlayer.playerConnection.sendPacket(metaPacket)
        entityPlayer.playerConnection.sendPacket(attachPacket)
    }

    PrefixTag.cachedEntity[player.uniqueId] = armorStand
}

fun Player.isVanish(): Boolean {
    for (value in this.getMetadata("vanished")) {
        if (value.asBoolean()) {
            return true
        }
    }

    return false
}