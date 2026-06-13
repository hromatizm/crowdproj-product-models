package com.product.model

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings

fun loadConfigAsMap(): Map<String, Any?> {
    val settings = LoadSettings.builder().build()
    val load = Load(settings)

    val inputStream = {}::class.java.getResourceAsStream("/application.yaml")
        ?: error("application.yaml not found on classpath")

    val root = load.loadFromInputStream(inputStream)

    @Suppress("UNCHECKED_CAST")
    return root as? Map<String, Any?>
        ?: error("Root of YAML is not a map")
}

fun Map<String, Any?>.getByPath(path: String): Any? {
    var current: Any? = this

    for (key in path.split('.')) {
        current = (current as? Map<String, Any?>)?.get(key) ?: return null
    }

    return current
}