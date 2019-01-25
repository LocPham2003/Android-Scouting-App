@file:Suppress("unused")

package ca.warp7.android.scouting.v5.entry

import android.util.Base64


data class V5TimedEntry(

    override val match: String,

    override val team: String,

    override val scout: String,

    override val board: Board,

    override val dataPoints: MutableList<DataPoint> = mutableListOf(),

    override var timestamp: Int,

    val getTime: () -> Int,

    override var comments: String = "",

    override var undone: Int = 0,

    val isTiming: Boolean = false

) : MutableEntry {

    override val encoded get() = "$match:$team:$scout1:${board.name}:$hexTimestamp:$undone:$encodedData:$comments1"

    override fun add(dataPoint: DataPoint) = this.dataPoints.add(index = nextIndex, element = dataPoint)

    override fun undo() = nextIndex.let { if (it == 0) null else dataPoints.removeAt(it - 1).also { undone++ } }

    override fun count(type: Int) = dataPoints.subList(0, nextIndex).filter { it.type == type }.size

    override fun lastValue(type: Int) = dataPoints.subList(0, nextIndex).lastOrNull { it.type == type }

    override fun focused(type: Int) = getTime().let { t -> dataPoints.any { it.type == type && it.time == t } }

    private val hexTimestamp get() = Integer.toHexString(timestamp)

    private val encodedData get() = Base64.encodeToString(dataPoints.flatten().toByteArray(), Base64.DEFAULT)

    private val comments1 get() = comments.replace("[^A-Za-z0-9 ]".toRegex(), "_")

    private val scout1 get() = scout.replace("[^A-Za-z0-9 ]".toRegex(), "_")

    private val nextIndex: Int
        get() {
            if (!isTiming) return dataPoints.size
            val relTime = getTime()
            var index = 0
            while (index < dataPoints.size && dataPoints[index].time <= relTime) index++
            return index
        }
}