/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer

enum class MinerState {
    WAITING, WALKING, MINING, STUCK
}

enum class ThoughtState {
    NO_TARGET, FAR_TARGET, CLOSE_TARGET
}

enum class ScanState {
    WIDE_SCAN, NARROW_SCAN
}

data class State(var minerState: MinerState, var thoughtState: ThoughtState, var scanState: ScanState) {
    override fun toString(): String {
        return "${minerState.name}, ${thoughtState.name}, ${scanState.name}"
    }
}

// Love jaz ;)