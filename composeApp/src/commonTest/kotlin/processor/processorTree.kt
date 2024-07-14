package processor

import kotlin.test.Test
import kotlin.test.assertEquals


class ProcessorTest {
    @Test
    fun canaryTest() {
        assertEquals(2, 2)
    }

    @Test
    fun traverseLevelsForTraceTreeShouldTraverseTraceTreeCorrectly() {

        val l5f1 = TraceTree(
            data = TraceNode(
                functionName = "java.lang.reflect.Method.invoke(Native Method)",
                firstCaptureTime = 1719551931975,
                lastCaptureTime = 1719551931975
            ),
            children = mutableListOf()
        )

        val l4f1 = TraceTree(
            data = TraceNode(
                functionName = "com.airgo.pilot.profile.performanceLoyaltyContainer.PerformanceLoyaltyContainerContainerComponent.init(PerformanceLoyaltyContainerContainerComponent.java:45)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551931857
            ),
            children = mutableListOf()
        )

        val l4f2 = TraceTree(
            data = TraceNode(
                functionName = "com.airgo.pilot.profile.performanceLoyaltyContainer.PerformanceLoyaltyContainerContainerComponent.init(PerformanceLoyaltyContainerContainerComponent.java:45)",
                firstCaptureTime = 1719551931975,
                lastCaptureTime = 1719551931975
            ),
            children = mutableListOf(l5f1)
        )

        val l3f1 = TraceTree(
            data = TraceNode(
                functionName = "androidx.fragment.app.FragmentManager.executeOpsTogether(FragmentManager.java:1899)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551931975
            ),
            children = mutableListOf(l4f1)
        )

        val l3f2 = TraceTree(
            data = TraceNode(
                functionName = "androidx.fragment.app.FragmentManager.executeOpsTogether(FragmentManager.java:1912)",
                firstCaptureTime = 1719551932089,
                lastCaptureTime = 1719551932089
            ),
            children = mutableListOf(l4f2)
        )

        val l3f3 = TraceTree(
            data = TraceNode(
                functionName = "android.view.Choreographer.doCallbacks(Choreographer.java:796)",
                firstCaptureTime = 1719551932206,
                lastCaptureTime = 1719551932206
            ),
            children = mutableListOf()
        )


        val l2f1 = TraceTree(
            data = TraceNode(
                functionName = "android.os.Handler.handleCallback(Handler.java:938)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551932089
            ),
            children = mutableListOf(l3f1, l3f2)
        )

        val l2f2 = TraceTree(
            data = TraceNode(
                functionName = "androidx.fragment.app.FragmentManager$5.run(FragmentManager.java:547)",
                firstCaptureTime = 1719551932089,
                lastCaptureTime = 1719551932206
            ),
            children = mutableListOf(l3f3)
        )

        val l1f1 = TraceTree(
            data = TraceNode(
                functionName = "com.android.internal.os.RuntimeInit\$MethodAndArgsCaller.run(RuntimeInit.java:603)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551932206
            ),
            children = mutableListOf(l2f1, l2f2)
        )

        val root = TraceTree(
            data = TraceNode(
                functionName = "com.android.internal.os.ZygoteInit.main(ZygoteInit.java:947)",
                firstCaptureTime = 1719551931857,
                lastCaptureTime = 1719551932206
            ),
            children = mutableListOf(l1f1)
        )

        val actualValue = traverseLevelsForTraceTree(root)

        val expectedValue = hashMapOf<Int, List<TraceNode>>()
        expectedValue[0] = listOf(root.data)
        expectedValue[1] = listOf(l1f1.data)
        expectedValue[2] = listOf(l2f1.data, l2f2.data)
        expectedValue[3] = listOf(l3f1.data, l3f2.data, l3f3.data)
        expectedValue[4] = listOf(l4f1.data, l4f2.data)
        expectedValue[5] = listOf(l5f1.data)

        assertEquals(expectedValue, actualValue)
    }
}
