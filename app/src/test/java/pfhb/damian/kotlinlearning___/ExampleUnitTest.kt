package pfhb.damian.kotlinlearning___

import org.junit.*

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    private val manager = Manager()
    private val user1 = manager.newUser("Johny", 1200.35F, "j0hny", 3333)
    private val user2 =manager.newUser("Thomas", 200.30F, "Th0m@s", 5555)
    private val user3 = manager.newUser("Janush", 100F, "jnsh", 9876)

    @Test
    fun `return true if successful transaction`() {
        assertEquals(true, manager.makeTransaction(user1, user2, "j0hny", 500f))
    }

    @Test
    fun `return false if put bad password in transaction`() {
        assertEquals(false, manager.makeTransaction(user1, user2, "bad_pwd", 500f))
    }

    @Test
    fun `return false if user has not cash to make transaction`() {
        assertEquals(false, manager.makeTransaction(user1, user2, "j0hny", 5000f))
    }

    @Test
    fun `return user MDM by searching user by string`() {
        assertEquals(user1, manager.getUserByName("Johny"))
    }

    @Test
    fun `return null if there is no users in searching user by string`() {
        assertEquals(null, manager.getUserByName("abc"))
    }

    @Test
    fun `return true if is a good calculate of paying interest`(){
        val result = manager.payInterest(user3, 10F)
        assertEquals(110F, result)
    }

    @Test
    fun `calculate time with creating  1 000 000 and paying them interest`(){

        manager.clearData()
        val index = 0
        val name = index.toString()
        for(number in 1..10000000){
            manager.newUser(name, 500F, "pwd", 3333)
        }
        val j = payInterest()
        assertEquals(10000000, j)
    }

    private fun payInterest() : Int{
        var j = 0
        for (user in manager.getContainerOfData().container) {
            j++
            manager.payInterest(user, 10F)
        }
        return j
    }

}