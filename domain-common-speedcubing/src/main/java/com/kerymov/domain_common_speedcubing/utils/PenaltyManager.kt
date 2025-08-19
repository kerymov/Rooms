package com.kerymov.domain_common_speedcubing.utils

import com.kerymov.domain_common_speedcubing.models.Penalty

interface PenaltyManager {

    fun updateTimeWithPenalty(
        time: Long,
        currentPenalty: Penalty,
        newPenalty: Penalty
    ): Long
}