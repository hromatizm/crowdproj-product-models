package com.product.model.piugins

import com.product.model.ConfigPaths
import com.product.model.PmRepoInMemory
import com.product.model.RepoPmCassandra
import com.product.model.config.CassandraConfig
import com.product.model.repo.IRepoPm
import io.ktor.server.application.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getDatabaseConf(type: PmDbType): IRepoPm {
    val dbSettingPath = "${ConfigPaths.REPOSITORY}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', " +
                    "but it must be set in application.yml to one of: 'inmemory', 'cassandra'"
        )
    }
}

fun Application.initInMemory(): IRepoPm {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return PmRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}


private fun Application.initCassandra(): IRepoPm {
    val config = CassandraConfig(environment.config)
    return RepoPmCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}


enum class PmDbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}