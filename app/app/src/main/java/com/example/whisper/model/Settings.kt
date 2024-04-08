package com.example.whisper.model
data class Settings(
    val language: Language = Language.ENGLISH
)

// Define i18n languages with display names and language code
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