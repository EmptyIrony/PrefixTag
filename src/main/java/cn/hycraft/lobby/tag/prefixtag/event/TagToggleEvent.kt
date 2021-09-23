package cn.hycraft.lobby.tag.prefixtag.event

import cn.hycraft.lobby.tag.prefixtag.data.PlayerInfo
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TagToggleEvent(val info: PlayerInfo): Event(){
    companion object{
        @JvmStatic
        var HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }

    }

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }

    fun callEvent() {
        Bukkit.getPluginManager()
            .callEvent(this);
    }
}