package cn.hycraft.lobby.tag.prefixtag

import cn.hycraft.lobby.tag.prefixtag.data.Tag
import com.qrakn.honcho.command.CommandMeta
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["tag"])
class PrefixTagCommand {

    fun execute(player: Player) {

    }

    fun execute(sender: CommandSender, argA: String, argB: String) {
        val player = Bukkit.getPlayer(argA) ?: return


        if (sender.hasPermission("hycraft.admin")) {
            val tag = Tag.cache[argB] ?: return

        }
    }

}