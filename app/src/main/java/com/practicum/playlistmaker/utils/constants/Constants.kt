package com.practicum.playlistmaker.utils.constants

object Constants {
    const val SEARCH_STRING = "SEARCH_STRING"
    const val SEARCH_DEF = ""
    const val TRACKS_NOT_FOUND = "Ничего не нашлось"
    const val TRACKS_NOT_FOUND_2 = "Ничего не найдено"
    const val NETWORK_PROBLEM = "Проблемы со связью\n" +
            "\n" +
            "Загрузка не удалась. Проверьте подключение к интернету"
    const val SOMETHING_WRONG = "Что-то пошло не так.."
    const val SEARCH_SUCCESS = "Поиск успешно произведен!"
    const val HISTORY_CLEARED ="История поиска была удалена"

    const val SEARCH_DEBOUNCE_DELAY = 2000L

    const val SEARCH_HISTORY_KEY = "history"
    const val HISTORY_CAPACITY = 10
}