package com.product.model

import com.product.model.repo.IRepoPm
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun initCorSettings(env: Map<String, Any?>) = CorSettings(
    repoTest = getDatabaseConf(PmDbType.TEST, env),
    repoProd = getDatabaseConf(PmDbType.PROD, env),
    repoStub = PmRepoStub(),
)

private fun getDatabaseConf(type: PmDbType, env: Map<String, Any?>): IRepoPm {
    val dbSettingPath = "${ConfigPaths.REPOSITORY}.${type.confName}"
    val dbSetting = env.getByPath(dbSettingPath) as? String
    return when (dbSetting?.lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory(env)
        "cassandra", "nosql", "cass" -> initCassandra(env)
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', " +
                    "but it must be set in application.yml to one of: 'inmemory', 'cassandra'"
        )
    }
}

fun initInMemory(env: Map<String, Any?>): IRepoPm {
    val ttlSetting = (env.getByPath("db.prod") as? String)?.let {
        Duration.parse(it)
    }
    return PmRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}


private fun initCassandra(env: Map<String, Any?>): IRepoPm {
    val configPath = "${ConfigPaths.REPOSITORY}.${"cassandra"}"
    val config = env.getByPath(configPath) as Map<String, String>
    return RepoPmCassandra(
        keyspaceName = config["keyspace"] ?: "product_model_ks",
        host = config["host"] ?: "localhost",
        port = config["port"]?.toInt() ?: 9042,
        user = config["user"] ?: "cassandra",
        pass = config["pass"] ?: "cassandra",
    )
}

enum class PmDbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}
