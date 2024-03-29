import Persistence.JSONSerializer
import controllers.NoteAPI
import models.Note
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit


private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))


fun main(args: Array<String>) {
    runMenu()

}


fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > -----------------------------------------------------------------------
         > |                            NOTE KEEPER APP                          |
         > -----------------------------------------------------------------------
         > |           NOTE MENU                |   |      (6) Make Note Public  |
         > |      (1) Add a note                |   |      (7) Search By Title   |
         > |      (2) List all notes            |   |      (8) Search By Owner   |   
         > |      (3) Update a note             |   |                            |
         > |      (4) Delete a note             |   |      (20) Save notes       |
         > |      (5) Archive a Note            |   |      (21) Load notes       |  
         > -----------------------------------------------------------------------
         > |                              (0) Exit                               |
         > -----------------------------------------------------------------------
         > ==>> """.trimMargin(">"))
}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archive()
            6  -> public()
            7  ->searchNotes()
            8  ->searchOwner()
           20  ->save()
           21  ->load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    if (validateInput(noteTitle)) {                             //validation make sure strings are 3 in length
                print("")
    }else{
        println("Not a valid Title---Try again:")
            addNote()
    }

    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")

    val noteCategory = readNextLine("Enter a category for the note: ")
    if (validateInput(noteCategory)) {
        print("")
    }else{
        println("Not a valid Category---Try again:")
        addNote()
    }

    val noteOwner = readNextLine("Enter a Name for the note: ")
    if (validateInput(noteOwner)) {
        print("")
    }else{
        println("Not a valid Name---Try again:")
        addNote()
    }

    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, noteOwner, false,  false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun validateInput(input: String): Boolean{
    return input.length >=3
}



fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > |   4) View Public  notes      |
                  > |   5) View Private  notes     |

                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            4 -> listPublicNotes();
            5 -> listPrivateNotes();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}


fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

fun deleteNote() {
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}



        fun updateNote() {
            //logger.info { "updateNotes() function invoked" }
            listNotes()
            if (noteAPI.numberOfNotes() > 0) {
                //only ask the user to choose the note if notes exist
                val indexToUpdate = readNextInt("Enter the index of the note to update: ")
                if (noteAPI.isValidIndex(indexToUpdate)) {
                    val noteTitle = readNextLine("Enter a title for the note: ")//validation make sure strings are 3 in length
                    if (validateInput(noteTitle)) {
                        print("")
                    }else{
                        println("Not a valid Title---Try again:")
                        updateNote()
                    }
                    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
                    val noteCategory = readNextLine("Enter a category for the note: ")
                    if (validateInput(noteCategory)) {
                        print("")
                    }else{
                        println("Not a valid Category---Try again:")
                        updateNote()
                    }
                    val noteOwner = readNextLine("Enter a Name for the note: ")
                    if (validateInput(noteOwner)) {
                        print("")
                    }else{
                        println("Not a valid Name---Try again:")
                        updateNote()
                    }

                    //pass the index of the note and the new note details to NoteAPI for updating and check for success.
                    if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, noteOwner, false, false))) {
                        println("Update Successful")
                    } else {
                        println("Update Failed")
                    }
                } else {
                    println("There are no notes for this index number")
                }
            }
        }

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun archive() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archive(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}

fun searchNotes() { // search notes by title
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

fun searchOwner() { // search for a note by owner name
    val searchName = readNextLine("Enter the Name to search by: ")
    val searchResult = noteAPI.searchByOwner(searchName)
    if (searchResult.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResult)
    }
}

fun public() { // converts a private note to public
    listPrivateNotes()
    if (noteAPI.numberOfPrivateNotes() > 0) {
        val indexToPublic = readNextInt("Enter the index of the note to make Public: ")
        //pass the index of the note to NoteAPI to make public and check for success.
        if (noteAPI.public(indexToPublic)) {
            println("Save Successful!")
        } else {
            println("Save NOT Successful")
        }
    }

}

fun listPrivateNotes() { //list all private notes
    println(noteAPI.listPrivateNotes())
}

fun listPublicNotes() { // list all public notes
    println(noteAPI.listPublicNotes())
}

