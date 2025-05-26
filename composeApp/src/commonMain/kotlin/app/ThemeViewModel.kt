package app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.theme.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _isDark = MutableStateFlow(false)
    val isDark: StateFlow<Boolean> = _isDark

    init {
        viewModelScope.launch {
            val savedTheme = themeRepository.isDarkTheme()
            if (_isDark.value != savedTheme) {
                _isDark.value = savedTheme
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newValue = !_isDark.value
            themeRepository.setDarkTheme(newValue)
            _isDark.value = newValue
            println("🌗 New theme value = $newValue")
        }
    }
}