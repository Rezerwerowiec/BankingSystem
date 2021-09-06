package pfhb.damian.kotlinlearning___

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    private val manager = Manager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ModelPreferencesManager.with(this)
        loadData()

        val user1 = manager.getUserByName("Johny") ?: return
        manager.newCredit(user1, 10000f, 12, 6.5f)
        manager.newCredit(user1, 1000f, 3, 4.5f)
        creditInfo(user1)
        userInfo(user1)
        saveData()

    }

    private fun creditInfo(user : MainDataModel){
        val creditView = findViewById<TextView>(R.id.creditInfo)
        creditView.text = manager.getCreditInfoFromList(manager.getUserCredits(user))
    }

    private fun userInfo(user: MainDataModel){
        val resultView = findViewById<TextView>(R.id.resultView)


        resultView.text = user.getData()

    }


    private fun loadData(){
        val container = ModelPreferencesManager.get<MainDataModelContainer>("KEY_CONTAINER")
        if (container != null) {
            manager.loadDataFromContainerOfData(container)
        }
        val creditContainer = ModelPreferencesManager.get<CreditDataContainer>("KEY_CONTAINERCREDITS")
        if (creditContainer != null) {
            manager.loadDataFromContainerOfCredits(creditContainer)
        }
    }

    private fun saveData(){
        ModelPreferencesManager.put(manager.getContainerOfData(), "KEY_CONTAINER")
        ModelPreferencesManager.put(manager.getContainerOfCredits(), "KEY_CONTAINERCREDITS")
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