package processor

import sampleTraceInterval
import kotlin.test.Test
import kotlin.test.assertEquals


class ProcessorTest {

    @Test
    fun canaryTest() {
        assertEquals(2, 2)
    }
    
    @Test
    fun testPreProcessorShouldParseFixtureCorrectly() {
        assertEquals(sampleTraceInterval.traceData.size, 18)
    }

}