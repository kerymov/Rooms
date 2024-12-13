package com.example.rooms.presentation.features.room.utils

data class ResultInfo(
    val time: Long,
    val isDnf: Boolean
) : Comparable<ResultInfo> {
    override fun compareTo(other: ResultInfo): Int {
        return when {
            this.isDnf && !other.isDnf -> 1
            !this.isDnf && other.isDnf -> -1
            else -> (this.time - other.time).toInt()
        }
    }
}

data class Statistic(
    val bestTime: StatisticInfo,
    val worstTime: StatisticInfo,
    val mean: StatisticInfo,
    val averageOfFive: StatisticInfo,
    val averageOfTwelve: StatisticInfo,
    val averageOfFifty: StatisticInfo,
    val averageOfHundred: StatisticInfo
)

sealed interface StatisticInfo

data class Completed(val time: Long) : StatisticInfo
data class DnfSingle(val time: Long) : StatisticInfo
data object DnfAverage : StatisticInfo
data object NoValue : StatisticInfo

object StatisticManager {

    fun getStatistic(results: List<ResultInfo>): Statistic {
        val bestTime = findBestResult(results)
        val worstTime = findWorstResult(results)
        val mean = calculateMean(results)
        val averageOfFive = calculateLastAverageOf(5, results)
        val averageOfTwelve = calculateLastAverageOf(12, results)
        val averageOfFifty = calculateLastAverageOf(50, results)
        val averageOfHundred = calculateLastAverageOf(100, results)

        return Statistic(
            bestTime = bestTime,
            worstTime = worstTime,
            mean = mean,
            averageOfFive = averageOfFive,
            averageOfTwelve = averageOfTwelve,
            averageOfFifty = averageOfFifty,
            averageOfHundred = averageOfHundred
        )
    }

    private fun findBestResult(results: List<ResultInfo>): StatisticInfo {
        val bestTime = results.minOrNull() ?: return NoValue

        return if (bestTime.isDnf) return DnfSingle(bestTime.time) else Completed(bestTime.time)
    }

    private fun findWorstResult(results: List<ResultInfo>): StatisticInfo {
        val worstTime = results.maxOrNull() ?: return NoValue

        return if (worstTime.isDnf) return DnfSingle(worstTime.time) else Completed(worstTime.time)
    }

    private fun calculateLastAverageOf(n: Int, results: List<ResultInfo>): StatisticInfo {
        if (results.size < n) return NoValue

        val lastNResults = results.takeLast(n)

        val dnfResults = lastNResults.filter { it.isDnf }
        if (dnfResults.size >= 2) return DnfAverage

        val averageTime = lastNResults
            .sorted()
            .drop(1)
            .dropLast(1)
            .sumOf { it.time } / (lastNResults.size - 2)

        return Completed(averageTime)
    }

    private fun calculateMean(results: List<ResultInfo>): StatisticInfo {
        if (results.isEmpty()) return NoValue

        val dnfResults = results.filter { it.isDnf }
        if (dnfResults.size == results.size) return DnfAverage

        val successfulResults = results.filter { !it.isDnf }
        val meanTime = successfulResults.sumOf { it.time } / successfulResults.size

        return Completed(meanTime)
    }
}
