package processor

import sampleTraceInterval
import kotlin.test.Test
import kotlin.test.assertEquals


class ParserTest {

    @Test
    fun canaryTest() {
        assertEquals(2, 2)
    }

    @Test
    fun testPreProcessorShouldParseFixtureCorrectly() {
        assertEquals(sampleTraceInterval.traceData.size, 18)
    }

    @Test
    fun FromTraceIntervalTreeShouldBeConstructedCorrectly() {

        val actualValue = constructTreeFromStackTraces(sampleTraceInterval)

        val expectedRoot = TraceTree(
            data = TraceNode(
                functionName = "com.android.internal.os.ZygoteInit.main(ZygoteInit.java:947)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551933960,
            ),
            children = mutableListOf()
        )

        val expectedL7Node = TraceNode(
            functionName = "android.os.Handler.handleCallback(Handler.java:938)",
            firstCaptureTime = 1719551931857,
            lastCaptureTime = 1719551933960,
        )

        val lastExpectedL8F8Node = TraceNode(
            functionName = "android.view.Choreographer\$FrameDisplayEventReceiver.run(Choreographer.java:957)",
            firstCaptureTime = 1719551933960,
            lastCaptureTime = 1719551933960,
        )

        val lastExpectedL8F5Node = TraceNode(
            functionName = "io.reactivex.android.schedulers.HandlerScheduler\$ScheduledRunnable.run(HandlerScheduler.java:124)",
            firstCaptureTime = 1719551933341,
            lastCaptureTime = 1719551933341,
        )

        val lastExpectedL14F5Node = TraceNode(
            functionName = "android.view.ViewRootImpl.performTraversals(ViewRootImpl.java:2557)",
            firstCaptureTime = 1719551932691,
            lastCaptureTime = 1719551932923,
        )

        assertEquals(expectedRoot.data, actualValue.data)
        assertEquals(
            expectedL7Node,
            actualValue.children[0].children[0].children[0].children[0].children[0].children[0].data
        )
        assertEquals(
            lastExpectedL8F8Node,
            actualValue.children[0].children[0].children[0].children[0].children[0].children[0].children[7].data
        )
        assertEquals(
            lastExpectedL8F5Node,
            actualValue.children[0].children[0].children[0].children[0].children[0].children[0].children[4].data
        )
        assertEquals(
            lastExpectedL14F5Node,
            actualValue.children[0].children[0].children[0].children[0].children[0].children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].data
        )
    }

}