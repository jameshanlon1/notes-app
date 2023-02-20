package models

data class Note(val noteTitle: String,
                val notePrice: Int,
                val noteCategoty: String,
                val isNoteArchived: Boolean){
}