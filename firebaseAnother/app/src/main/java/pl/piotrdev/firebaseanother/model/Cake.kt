package pl.piotrdev.firebaseanother.model

import androidx.constraintlayout.solver.widgets.Snapshot
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Cake(private val snapshot: DataSnapshot?) {
    lateinit var id: String
    lateinit var name: String
    lateinit var dateBaked: String
    lateinit var expiryDate: String



    init {
        try {
            if (snapshot != null) {
                createPokemonFromSnapshot(snapshot)
            }
        } catch( e: Exception) {
            e.printStackTrace()
        }
    }


    private fun createPokemonFromSnapshot(snapshot: DataSnapshot) {
        try {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            name= data["name"] as String
            dateBaked = data["dateBaked"] as String
            expiryDate = data["expiryDate"] as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toMap(): HashMap<String, Any> {
        val map: HashMap<String, Any> = HashMap()
        map["name"] = name
        map["dateBaked"] = dateBaked
        map["expiryDate"] = expiryDate
        return map
    }
}