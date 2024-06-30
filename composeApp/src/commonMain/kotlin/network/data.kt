package network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewInterval(
    @SerialName("startTime") var startTime: Long,
    @SerialName("endTime") var endTime: Long,
    @SerialName("lastKnownTime") var lastKnownTime: Int? = null,
    @SerialName("anrSampleList") var anrSampleList: List<AnrSampleList>,
    @SerialName("anrHash") var anrHash: String
)

@Serializable
data class AnrSampleList(
    @SerialName("timestamp") var timestamp: Long,
    @SerialName("threads") var threads: List<Threads>,
    @SerialName("sampleOverheadMs") var sampleOverheadMs: Int? = null,
    @SerialName("code") var code: Int? = null

)

@Serializable
data class Threads(
    @SerialName("threadId") var threadId: Int,
    @SerialName("name") var name: String,
    @SerialName("state") var state: String,
    @SerialName("priority") var priority: Int,
    @SerialName("lines") var lines: String
)


@Serializable
data class Interval(
    val interval_hash: String,
    val start_time: Long,
    val end_time: Long,
    val capture_time: Long,
    val thread_name: String,
    val stack_trace: String
)

@Serializable
data class IntervalResponse(
    val success: Boolean,
    val data: IntervalData
)

@Serializable
data class IntervalData(
    val intervals: List<NewInterval>
)


