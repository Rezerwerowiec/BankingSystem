package pfhb.damian.kotlinlearning___

import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //assertEquals("pfhb.damian.kotlinlearning___", appContext.packageName)

        val manager = Manager()
        val user1 = manager.newUser("Johny", 1200.35F, "j0hny", 1111)
        val user2 =manager.newUser("Thomas", 200.30F, "Th0m@s", 1111)

        assertEquals(true, manager.makeTransaction(user1, user2, "j0hny", 500f))
    }
}