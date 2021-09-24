package cn.hycraft.lobby.tag.prefixtag.dao.factory

import cn.hycraft.lobby.tag.prefixtag.PrefixTag
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.File
import java.io.IOException


class HikariDataSourceFactory : UnpooledDataSourceFactory() {

    companion object {
        @JvmStatic
        private lateinit var sqlSessionFactory: SqlSessionFactory

        @JvmStatic
        fun getSqlSessionFactory(): SqlSessionFactory {
            if (!this::sqlSessionFactory.isInitialized) {
                val resource = "mybatis-config.xml"
                try {
                    sqlSessionFactory = SqlSessionFactoryBuilder().build(
                        PrefixTag.INSTANCE.javaClass.classLoader.getResourceAsStream(resource)
                    )
                    return sqlSessionFactory
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return sqlSessionFactory
        }
    }

    init {
        val config = HikariConfig("${PrefixTag.INSTANCE.dataFolder}/hikari.properties")
        this.dataSource = HikariDataSource(config)
    }
}