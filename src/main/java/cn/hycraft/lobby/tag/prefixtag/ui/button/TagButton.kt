package cn.hycraft.lobby.tag.prefixtag.ui.button

import cn.hycraft.core.hycraftcore.util.ItemBuilder
import cn.hycraft.core.hycraftcore.util.menu.Button
import cn.hycraft.lobby.tag.prefixtag.PrefixTag
import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import cn.hycraft.lobby.tag.prefixtag.data.Tag
import cn.hycraft.lobby.tag.prefixtag.refreshTag
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class TagButton(private val tag: Tag, private val info: PlayerInfo): Button(){
    override fun getButtonItem(player: Player): ItemStack {
        val builder = ItemBuilder(tag.icon)
            .name(tag.displayName)
            .lore("")
            .lore(tag.descriptions)
            .lore("")

        if (tag == info.currentTag) {
            builder.lore("&c取下该称号")
        } else {
            builder.lore("&e点击佩戴")
        }
        return builder.build()
    }

    override fun clicked(player: Player, p1: Int, p2: ClickType?, p3: Int, p4: ItemStack?) {
        if (tag == info.currentTag) {
            info.currentTag = null
        } else {
            info.currentTag = tag
        }
        player.refreshTag()
        player.playSound(player.location, Sound.LEVEL_UP, 1.5F, 1.5F)
        player.closeInventory()
        PrefixTag.getPlayerService().savePlayerInfoWear(player.uniqueId)
    }
}