package com.example.whisper.model
data class Settings(
    val language: Language = Language.ENGLISH,
    val theme: Theme = Theme.LIGHT
)

// Enumerate available languages in the settings
enum class Language(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    TAGALOG("Tagalog", "tl");

    companion object {
        fun getDisplayNameByOrdinal(ordinal: Int): String {
            val language = entries.getOrNull(ordinal) ?: return "English"
            return language.displayName
        }

        fun getByCode(code: String): Language {
            return when(code) {
                "en" -> ENGLISH
                "tl" -> TAGALOG
                else -> ENGLISH
            }
        }
    }
}

// Enumerate available themes in the settings
enum class Theme(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    WARM("Warm"),
    COOL("Cool");

    companion object {
        fun getDisplayNameByOrdinal(ordinal: Int): String {
            return entries.getOrNull(ordinal)?.displayName ?: LIGHT.displayName
        }
    }
}
