/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe

import net.ultradev.prisoncore.rewards.RewardApplicator
import java.math.BigInteger

data class EnchantmentResult(
    var cancelled: Boolean,
    var pickaxeXP: Int,
    var rewards: MutableList<RewardApplicator>,
    var runnables: MutableList<PlayerRunnable>,
    var blocksMined: BigInteger
) {
    fun add(res: EnchantmentResult) {
        this.pickaxeXP += res.pickaxeXP
        this.rewards.addAll(res.rewards)
        this.runnables.addAll(res.runnables)
        this.blocksMined = this.blocksMined.add(res.blocksMined)
    }
}