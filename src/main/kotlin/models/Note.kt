package models

data class Note(val noteTitle: String,
                val notePriority: Int,
                val noteCategoty: String,
                val isNoteArchived: Boolean){
}