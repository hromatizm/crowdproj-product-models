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
    val host = env["host"] as? String

    val dbSetting = env.getByPath(dbSettingPath) as? String
    return when (dbSetting?.lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory(env)
        // "cassandra", "nosql", "cass" -> initCassandra()
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


//private fun Application.initCassandra(): IRepoPm {
//    val config = CassandraConfig(environment.config)
//    return RepoAdCassandra(
//        keyspaceName = config.keyspace,
//        host = config.host,
//        port = config.port,
//        user = config.user,
//        pass = config.pass,
//    )
//}


enum class PmDbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}
