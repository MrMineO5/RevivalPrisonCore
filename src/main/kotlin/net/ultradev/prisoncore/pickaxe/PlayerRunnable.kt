/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe

import org.bukkit.entity.Player

interface PlayerRunnable {
    fun run(player: Player)
}