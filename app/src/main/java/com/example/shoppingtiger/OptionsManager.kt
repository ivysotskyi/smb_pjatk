import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object OptionsManager {
    private const val KEY_CURRENCY = "currency"
    private const val KEY_SHOW_CHECKBOX = "showCheckbox"

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun getCurrency(): String {
        return preferences.getString(KEY_CURRENCY, "ZŁ") ?: "ZŁ"
    }

    fun setCurrency(currency: String) {
        preferences.edit {
            putString(KEY_CURRENCY, currency)
        }
    }

    fun getShowCheckbox(): Boolean {
        return preferences.getBoolean(KEY_SHOW_CHECKBOX, true)
    }

    fun setShowCheckbox(showCheckbox: Boolean) {
        preferences.edit {
            putBoolean(KEY_SHOW_CHECKBOX, showCheckbox)
        }
    }
}
