package cn.hycraft.lobby.tag.prefixtag

import cn.hycraft.common.colored
import cn.hycraft.common.util.dependencies.Dependency
import cn.hycraft.common.util.dependencies.DependencyManager
import cn.hycraft.common.util.dependencies.loaders.LoaderType
import cn.hycraft.common.util.dependencies.loaders.ReflectionClassLoader
import cn.hycraft.core.hycraftcore.HyCraftSpigotCore
import cn.hycraft.lobby.tag.prefixtag.dao.factory.HikariDataSourceFactory
import cn.hycraft.lobby.tag.prefixtag.dao.service.PlayerService
import cn.hycraft.lobby.tag.prefixtag.dao.service.impl.PlayerServiceImpl
import cn.hycraft.lobby.tag.prefixtag.data.Tag
import net.minecraft.server.v1_8_R3.EntityArmorStand
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

class PrefixTag: JavaPlugin(){
    companion object {
        @JvmStatic
        lateinit var INSTANCE: PrefixTag

        @JvmStatic
        private lateinit var playerService: PlayerService
        @JvmStatic
        fun getPlayerService(): PlayerService{
            if (this::playerService.isInitialized) {
                return playerService
            }
            playerService = PlayerServiceImpl()
            return playerService
        }

        @JvmStatic
        val cachedEntity = ConcurrentHashMap<UUID, EntityArmorStand>()
    }

    override fun onEnable() {
        INSTANCE = this

        //因为要读取配置文件, 所以必须用本插件的classLoader
        DependencyManager(dataFolder, ReflectionClassLoader(this)).loadDependencies(
            Dependency(
                "hikari",
                "com.zaxxer",
                "HikariCP",
                "4.0.3",
                LoaderType.REFLECTION
            ),
            Dependency(
                "slf4j-api",
                "org.slf4j",
                "slf4j-api",
                "1.7.30",
                LoaderType.REFLECTION
            ),
            Dependency(
                "mybatis",
                "org.mybatis",
                "mybatis",
                "3.5.7",
                LoaderType.REFLECTION
            )
        )

        HikariDataSourceFactory.getSqlSessionFactory()
        this.loadConfig()

        HyCraftSpigotCore.instance.honcho.registerCommand(PrefixTagCommand)
    }

    private fun loadConfig() {
        this.saveDefaultConfig()
        val section = config.getConfigurationSection("tags")
        for (key in section.getKeys(false)) {
            val tagConfig = section.getConfigurationSection(key)
            val displayName = tagConfig.getString("displayName") ?: continue
            val realDisplay = tagConfig.getString("realDisplay") ?: continue
            val descriptions = tagConfig.getStringList("descriptions") ?: continue
            val materialStr = tagConfig.getString("material") ?: continue

            val tag = Tag(key)
            tag.displayName = displayName
            tag.realDisplay = realDisplay
            tag.icon = Material.valueOf(materialStr)
            for (description in descriptions) {
                tag.descriptions.add(description.colored())
            }

            Tag.cache[key] = tag
        }
    }
}