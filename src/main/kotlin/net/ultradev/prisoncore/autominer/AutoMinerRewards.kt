/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer

import org.bukkit.configuration.ConfigurationSection

data class AutoMinerRewards(var tokens: Long, var keys: MutableMap<String, Long>, var socketGemDust: Long) {
    companion object {
        fun deserialize(conf: ConfigurationSection): AutoMinerRewards {
            val tokens: Long = if (conf.contains("tokens")) {
                conf.getLong("tokens")
            } else {
                0
            }
            val keys: MutableMap<String, Long> = mutableMapOf()
            if (conf.contains("keys")) {
                conf.getConfigurationSection("keys").getKeys(false).forEach {
                    keys[it] = conf.getLong("keys.$it")
                }
            }
            val socketGemDust: Long = if (conf.contains("socketGemDust")) {
                conf.getLong("socketGemDust")
            } else {
                0
            }
            return AutoMinerRewards(tokens, keys, socketGemDust)
        }

        fun blank(): AutoMinerRewards {
            return AutoMinerRewards(0, mutableMapOf(), 0)
        }
    }

    fun serialize(conf: ConfigurationSection) {
        conf.set("tokens", tokens)
        keys.entries.forEach {
            conf.set("keys." + it.key, it.value)
        }
        conf.set("socketGemDust", socketGemDust)
    }
}