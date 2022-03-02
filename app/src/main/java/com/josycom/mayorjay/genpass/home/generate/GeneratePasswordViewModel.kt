package com.josycom.mayorjay.genpass.home.generate

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.data.getPasswordCharacters
import com.josycom.mayorjay.genpass.data.getPasswordDisplayTexts
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import java.security.SecureRandom
import java.util.LinkedList
import java.util.Queue

class GeneratePasswordViewModel : ViewModel() {

    val passwordType = MutableLiveData<String>()
    val passwordLength = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val queue: Queue<PasswordData> = LinkedList()

    fun getPasswordTypes(): MutableList<String> = getPasswordDisplayTexts()

    fun validateInputs(): String {
        if (StringUtils.isBlank(passwordType.value) || StringUtils.equalsIgnoreCase(passwordType.value, Constants.SELECT_PASSWORD_TYPE)) {
            return Constants.SELECT_PASSWORD_TYPE
        }

        if (passwordLength.value == null) {
            return "Password Length cannot be blank"
        }

        if (passwordLength.value?.toInt() ?: 0 < 16 || passwordLength.value?.toInt() ?: 0 > 64) {
            return "Please input a value between 16 and 64"
        }
        return StringUtils.EMPTY
    }

    fun generatePassword(passwordType: String, passwordLength: Int): String {
        val buffer = StringBuffer()
        val characters = getPasswordCharacters(passwordType)
        val charactersLength = characters.length
        val secureRandom = SecureRandom()
        for (i in 0 until passwordLength) {
            val index = secureRandom.nextInt(charactersLength).toDouble()
            buffer.append(characters[index.toInt()])
        }
        return buffer.toString()
    }

    fun cachePassword(context: Context, passwordData: PasswordData) {
        val dataStore = PreferenceManager(context.dataStore)
        if (queue.size >= 10) {
            queue.remove()
        }
        queue.add(passwordData)
        for (item in queue) {
            viewModelScope.launch {
                dataStore.setPasswordPref(item.key, "${item.password}-${item.timeGenerated}")
            }
        }
    }
}