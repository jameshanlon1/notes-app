package controllers

import models.Note

class NoteAPI {
    private var notes = ArrayList<Note>()


    fun add(note: Note): Boolean {
        return notes.add(note)
    }


    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

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

    fun listActiveNotes(): String {
        return if (numberOfActiveNotes() == 0) {
            "No active notes stored"
        } else {
            var listActiveNotes = ""
            for (note in notes) {
                if(!note.isNoteArchived) {
                    listActiveNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listActiveNotes
        }
    }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() == 0) {
            "No archived notes stored"
        } else {
            var listArchivedNotes = ""
            for (note in notes) {
                if(note.isNoteArchived) {
                    listArchivedNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listArchivedNotes
        }
    }

    fun numberOfArchivedNotes(): Int {
        //helper method to determine how many archived notes there are
        var number = 0
        for (note in notes) {
            if (note.isNoteArchived) {
                number++
            }
        }
        return number
    }

    fun numberOfActiveNotes(): Int {
        //helper method to determine how many active notes there are
        return notes.size
    }


    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfNotes += "${i}: ${notes[i]} \n"
                }
            }
            if (listOfNotes.equals("")) {
                "No notes with priority $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }
        }
    }


    fun numberOfNotesByPriority(priority:Int ): Int {
        //helper method to determine how many notes there are of a specific priority
        var counter = 0
        for (note in notes) {
            if(note.notePriority == priority){
                counter++
            }
        }
return counter
    }
}