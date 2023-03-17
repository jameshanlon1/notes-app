package models

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var noteOwner: String,
    var isNoteArchived: Boolean){
}