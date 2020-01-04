/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems

import net.ultradev.prisoncore.utils.math.MathUtils
import org.bukkit.Color

data class SocketGemColor(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun deserialize(str: String): SocketGemColor? {
            val strs: List<String> = str.split(Regex("\\|"), 4)
            if (strs[0] != "SocketGemColor") {
                return null
            }
            return SocketGemColor(strs[1].toInt(), strs[2].toInt(), strs[3].toInt())
        }

        fun random(): SocketGemColor {
            return SocketGemColor(MathUtils.random(0, 255), MathUtils.random(0, 255), MathUtils.random(0, 255))
        }
    }

    override fun toString(): String {
        return "SocketGemColor|$red|$green|$blue"
    }

    fun toColor(): Color {
        return Color.fromRGB(red, green, blue)
    }
}