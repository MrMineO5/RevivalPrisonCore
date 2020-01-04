/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer

import net.ultradev.prisoncore.utils.logging.Debugger
import org.bukkit.configuration.ConfigurationSection
import org.jetbrains.annotations.NotNull
import java.math.BigInteger

data class AutoMinerData(
    var autominerTime: BigInteger,
    var blocksMined: BigInteger,
    var rewards: AutoMinerRewards,
    var upgrades: MutableMap<AutoMinerUpgrade, Int>,
    var skin: String
) {
    companion object {
        fun deserialize(conf: ConfigurationSection): AutoMinerData {
            Debugger.log("Deserializing AutoMinerData", "autoMinerData")
            Debugger.log("Deserializing autominerTime", "autoMinerData")
            val autominerTime = if (conf.contains("autominerTime")) {
                BigInteger(conf.getString("autominerTime"))
            } else {
                BigInteger.ZERO
            }
            Debugger.log("Deserializing blocksMined", "autoMinerData")
            val blocksMined = if (conf.contains("blocksMined")) {
                BigInteger(conf.getString("blocksMined"))
            } else {
                BigInteger.ZERO
            }
            Debugger.log("Deserializing rewards", "autoMinerData")
            val rewards: AutoMinerRewards = if (conf.contains("rewards")) {
                AutoMinerRewards.deserialize(conf.getConfigurationSection("rewards"))
            } else {
                AutoMinerRewards.blank()
            }
            Debugger.log("Deserializing upgrades", "autoMinerData")
            val upgrades: MutableMap<AutoMinerUpgrade, Int> = mutableMapOf()
            if (conf.contains("upgrades")) {
                conf.getConfigurationSection("upgrades").getKeys(false).forEach {
                    Debugger.log(
                        "Deserializing upgrade: " + it.toUpperCase() + " value: " + conf.getInt("upgrades.$it"),
                        "autoMinerData"
                    )
                    upgrades[AutoMinerUpgrade.valueOf(it.toUpperCase())] = conf.getInt("upgrades.$it")
                }
            }
            Debugger.log("Deserializing skin", "autoMinerData")
            val name: String = if (conf.contains("skin")) {
                conf.getString("skin")
            } else {
                "1234Mari"
            }
            return AutoMinerData(autominerTime, blocksMined, rewards, upgrades, name)
        }

        fun blank(): AutoMinerData {
            return AutoMinerData(BigInteger.ZERO, BigInteger.ZERO, AutoMinerRewards.blank(), mutableMapOf(), "1234Mari")
        }
    }

    fun serialize(conf: ConfigurationSection) {
        Debugger.log("Serializing AutoMinerData", "autoMinerData")
        Debugger.log("Serializing autominerTime", "autoMinerData")
        conf.set("autominerTime", autominerTime.toString())
        Debugger.log("Serializing blocksMined", "autoMinerData")
        conf.set("blocksMined", blocksMined.toString())
        Debugger.log("Serializing rewards", "autoMinerData")
        rewards.serialize(conf.createSection("rewards"))
        Debugger.log("Serializing upgrades", "autoMinerData")
        upgrades.forEach {
            Debugger.log("Serializing upgrade: ${it.key.name}", "autoMinerData")
            conf.set("upgrades.${it.key.name}", it.value)
        }
        Debugger.log("Serializing skin", "autoMinerData")
        conf.set("skin", skin)
    }

    @NotNull
    fun getUpgradeLevel(upgrade: AutoMinerUpgrade): Int? {
        if (!upgrades.containsKey(upgrade)) {
            return 0
        }
        return upgrades[upgrade]
    }

    fun setUpgradeLevel(upgrade: AutoMinerUpgrade, amount: Int) {
        upgrades.plus(Pair(upgrade, amount))
    }
}