package com.josycom.mayorjay.genpass.home.generate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.data.getPasswordCharacters
import com.josycom.mayorjay.genpass.data.getPasswordDisplayTexts
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import java.security.SecureRandom
import java.util.LinkedList
import java.util.Queue

class GeneratePasswordViewModel(val preferenceManager: IPreferenceManager) : ViewModel() {

    val passwordType = MutableLiveData<String>()
    val passwordLength = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordQueue: Queue<PasswordData> = LinkedList()
    var queueToList = listOf<PasswordData>()

    fun getPasswordTypes(): MutableList<String> = getPasswordDisplayTexts()

    fun validateInputs(passwordType: String?, passwordLength: String?): String {
        return when {
            StringUtils.isBlank(passwordType) || StringUtils.equalsIgnoreCase(passwordType, Constants.PASSWORD_TYPE) -> {
                Constants.SELECT_PASSWORD_TYPE
            }
            StringUtils.isBlank(passwordLength) -> {
                Constants.SPECIFY_PASSWORD_LENGTH
            }
            passwordLength?.toInt() ?: 0 !in 16..64 -> {
                Constants.INPUT_VALID_PASSWORD_LENGTH
            }
            else -> StringUtils.EMPTY
        }
    }

    fun generatePassword(passwordType: String?, passwordLength: Int): String {
        val buffer = StringBuffer()
        val characters = getPasswordCharacters(passwordType ?: StringUtils.EMPTY)
        val secureRandom = SecureRandom()
        for (i in 0 until passwordLength) {
            val index = secureRandom.nextInt(characters.length)
            buffer.append(characters[index])
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
                preferenceManager.setStringPreference(item.key, "${item.password}-${item.timeGenerated}")
            }
        }
    }
}

class GeneratePasswordViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GeneratePasswordViewModel(preferenceManager) as T
    }
}