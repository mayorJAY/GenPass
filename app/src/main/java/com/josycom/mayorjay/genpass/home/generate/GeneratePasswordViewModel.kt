package com.josycom.mayorjay.genpass.home.generate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

class GeneratePasswordViewModel(application: Application) : AndroidViewModel(application) {

    val passwordType = MutableLiveData<String>()
    val passwordLength = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordQueue: Queue<PasswordData> = LinkedList()
    var preferenceManager: PreferenceManager? = null
    var queueToList = listOf<PasswordData>()

    init {
        preferenceManager = PreferenceManager(application.dataStore)
    }

    fun getPasswordTypes(): MutableList<String> = getPasswordDisplayTexts()

    fun validateInputs(passwordType: String?, passwordLength: String?): String {
        if (StringUtils.isBlank(passwordType) || StringUtils.equalsIgnoreCase(passwordType, Constants.PASSWORD_TYPE)) {
            return Constants.SELECT_PASSWORD_TYPE
        }

        if (StringUtils.isBlank(passwordLength)) {
            return Constants.SPECIFY_PASSWORD_LENGTH
        }

        if (passwordLength?.toInt() ?: 0 < 16 || passwordLength?.toInt() ?: 0 > 64) {
            return Constants.INPUT_VALID_PASSWORD_LENGTH
        }
        return StringUtils.EMPTY
    }

    fun generatePassword(passwordType: String?, passwordLength: Int): String {
        val buffer = StringBuffer()
        val characters = getPasswordCharacters(passwordType ?: StringUtils.EMPTY)
        val charactersLength = characters.length
        val secureRandom = SecureRandom()
        for (i in 0 until passwordLength) {
            val index = secureRandom.nextInt(charactersLength).toDouble()
            buffer.append(characters[index.toInt()])
        }
        return buffer.toString()
    }

    fun getNextAvailableKey(): String {
        if (passwordQueue.size >= 10) {
            passwordQueue.remove()
        }
        queueToList = passwordQueue.toList()
        var key = StringUtils.EMPTY
        val keyList = mutableListOf<String>()
        for (item in queueToList) {
            keyList.add(item.key)
        }
        for (item in Constants.PASSWORD_KEY_LIST) {
            if (!keyList.contains(item)) {
                key = item
                break
            }
        }
        return key
    }

    fun cachePassword(passwordData: PasswordData) {
        passwordQueue.add(passwordData)
        for (item in passwordQueue) {
            viewModelScope.launch {
                preferenceManager?.setPasswordPref(item.key, "${item.password}-${item.timeGenerated}")
            }
        }
    }
}