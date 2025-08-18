package com.kerymov.ui_common_speedcubing.utils

import com.kerymov.domain_common_speedcubing.models.Penalty
import com.kerymov.domain_common_speedcubing.utils.PenaltyManager

class TimerPenaltyManager : PenaltyManager {

    override fun updateTimeWithPenalty(
        time: Long,
        currentPenalty: Penalty,
        newPenalty: Penalty
    ): Long {
        val timeWithPenalty = when (newPenalty) {
            Penalty.NO_PENALTY -> removePenalty(time, currentPenalty)
            Penalty.PLUS_TWO -> setPlusTwoPenalty(time, currentPenalty)
            Penalty.DNF -> setDnfPenalty(time, currentPenalty)
        }

        return timeWithPenalty
    }

    private fun setPlusTwoPenalty(time: Long, currentPenalty: Penalty): Long {
        if (currentPenalty == Penalty.PLUS_TWO) return time

        return addTwoSeconds(time)
    }

    private fun setDnfPenalty(time: Long, currentPenalty: Penalty): Long {
        if (currentPenalty == Penalty.PLUS_TWO) return subtractTwoSeconds(time)

        return time
    }

    private fun removePenalty(time: Long, currentPenalty: Penalty): Long {
        if (currentPenalty == Penalty.PLUS_TWO) return subtractTwoSeconds(time)

        return time
    }

    private fun addTwoSeconds(time: Long) = time + 2000L

    private fun subtractTwoSeconds(time: Long) = time - 2000L
}