package pfhb.damian.kotlinlearning___

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    private val manager = Manager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ModelPreferencesManager.with(this)
        val resultView = findViewById<TextView>(R.id.resultView)

//        loadData()
        val user1 = manager.newUser("Johny", 1200.35F, "j0hny", 3355)
//        val user2 =manager.newUser("Thomas", 200.30F, "Th0m@s", 3356)
//
//        val isSuccess = manager.makeTransaction(user1, user2, "j0hny", 500f)
//        if(!isSuccess) Toast.makeText(baseContext, manager.getExitCode(), Toast.LENGTH_LONG).show()
//        saveData()
//        resultView.text = manager.getAllUsersToString()

        manager.newCredit(user1, 10000f, 12, 6.5f)
        manager.newCredit(user1, 1000f, 3, 4.5f)

        resultView.text = manager.getCreditInfoFromList(manager.getUserCredits(user1))
    }

    private fun loadData(){
        val container = ModelPreferencesManager.get<MainDataModelContainer>("KEY_CONTAINER")
        if (container != null) {
            manager.loadDataFromContainerOfData(container)
        }
    }

    private fun saveData(){
        ModelPreferencesManager.put(manager.getContainerOfData(), "KEY_CONTAINER")
    }
}




object ModelPreferencesManager {

    //Shared Preference field used to save and retrieve JSON string
    lateinit var preferences: SharedPreferences

    //Name of Shared Preference file
    private const val PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME"

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun with(application: MainActivity) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}