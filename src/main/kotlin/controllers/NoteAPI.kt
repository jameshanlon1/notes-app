package controllers

import Persistence.Serializer
import models.Note

class NoteAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()


    fun add(note: Note): Boolean {
        return notes.add(note)
    }


    fun listAllNotes(): String =
        if (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0) "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived })

    fun listArchivedNotes(): String =
        if (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived })


    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }

    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter { note -> note.notePriority == priority })
            if (listOfNotes.equals("")) "No notes with priority: $priority"
            else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
        }

    fun numberOfNotesByPriority(priority: Int): Int = notes.count { note: Note -> note.notePriority == priority }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }


    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, notes);
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }


    fun archive(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    fun searchByTitle(searchString: String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })


    private fun formatListString(notesToFormat: List<Note>): String =   //use of function to stop repitition
        notesToFormat
            .joinToString(separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString()
            }


    fun searchByOwner(searchString: String) =                   //gets not by owner name
        formatListString(
            notes.filter { note -> note.noteOwner.contains(searchString, ignoreCase = true) })


    fun listPublicNotes(): String =                                     //list all public notes
        if (numberOfPublicNotes() == 0) "No Public notes stored"
        else formatListString(notes.filter { note -> note.isNotePublic })

    fun listPrivateNotes(): String =                                  // list all private notes
        if (numberOfPrivateNotes() == 0) "No Private notes stored"
        else formatListString(notes.filter { note -> !note.isNotePublic })


    fun numberOfPublicNotes(): Int = notes.count { note: Note -> note.isNotePublic } //counts number of public notes

    fun numberOfPrivateNotes(): Int = notes.count { note: Note -> !note.isNotePublic } // counts number of private notes

    fun public(indexToPublic: Int): Boolean { // checks note is private and allows conversion to public
        if (isValidIndex(indexToPublic)) {
            val noteToPublic = notes[indexToPublic]
            if (!noteToPublic.isNotePublic) {
                noteToPublic.isNotePublic = true
                return true
            }
        }
        return false
    }
}