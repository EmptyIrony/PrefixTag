package cn.hycraft.lobby.tag.prefixtag.ui

import cn.hycraft.core.hycraftcore.util.menu.Button
import cn.hycraft.core.hycraftcore.util.menu.Menu
import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import cn.hycraft.lobby.tag.prefixtag.ui.button.TagButton
import org.bukkit.entity.Player

class MainMenu: Menu() {
    override fun getTitle(p0: Player?): String {
        return "称号选择"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val info = PlayerInfo.cache[player.uniqueId] ?: return mutableMapOf()

        val map = HashMap<Int, Button>()
        for ((index, tag) in info.unlockedTag.withIndex()) {
            map[index] = TagButton(tag, info)
        }

        return map
    }
}