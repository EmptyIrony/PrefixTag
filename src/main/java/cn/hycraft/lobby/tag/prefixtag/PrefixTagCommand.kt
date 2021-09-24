package cn.hycraft.lobby.tag.prefixtag

import cn.hycraft.core.hycraftcore.sendTranslatedMsg
import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import cn.hycraft.lobby.tag.prefixtag.data.Tag
import cn.hycraft.lobby.tag.prefixtag.ui.MainMenu
import com.qrakn.honcho.command.CommandMeta
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["tag"])
class PrefixTagCommand {

    fun execute(player: Player) {
        MainMenu().openMenu(player)
    }

    fun execute(sender: CommandSender, argA: String, argB: String) {
        val player = Bukkit.getPlayer(argA) ?: return

        if (sender.hasPermission("hycraft.admin")) {
            val tag = Tag.cache[argB] ?: return
            val info = PlayerInfo.cache[player.uniqueId] ?: return

            val success = info.unlockedTag.add(tag)
            if (success) {
                player.sendTranslatedMsg("&a你获得了称号 ${tag.displayName}")
                PrefixTag.getPlayerService().savePlayerInfoUnlock(player.uniqueId)
            }
        }
    }
}