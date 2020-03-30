package pl.piotrdev.firebaseanother.model

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import java.util.*

object CakeModel: Observable() {


        private var m_valueDataListener: ValueEventListener? = null     // The data listener that gets the data from the database
        private var m_PersonList: ArrayList<Cake>? = ArrayList()    // Cake cache

        //gets the database location reference for later repeated use
        private fun getDatabaseRef(): DatabaseReference? {
            return FirebaseDatabase.getInstance().reference.child("cake")
        }

        //called on object initialisation
        init {
            if (m_valueDataListener != null) {

                getDatabaseRef()
                    ?.removeEventListener(m_valueDataListener)
            }
            m_valueDataListener = null
            Log.i("CakeModel", "dataInit line 27")


            m_valueDataListener = object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    try {
                        Log.i("CakeModel", "data updated line 28")
                        val data: ArrayList<Cake> = ArrayList()
                        if (dataSnapshot != null) {
                            for (snapshot: DataSnapshot in dataSnapshot.children) {
                                try {
                                    data.add(Cake(snapshot))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            m_PersonList = data
                            Log.i("CakeModel","data updated there are " + m_PersonList!!.size + " Cake in the list")
                            setChanged()
                            notifyObservers()
                        } else {
                            throw Exception("data snapshot is null line 31")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    if (p0 != null) {
                        Log.i("CakeModel", "line 33 Data update cancelled, err = ${p0.message}, detail = ${p0.details}")
                    }
                }
            }
            getDatabaseRef()
                ?.addValueEventListener(m_valueDataListener)
        }

        fun getData(): ArrayList<Cake>? {
            return m_PersonList
        }

        fun saveCake(c: Cake, onComplete: OnCompleteListener<Void>? ) {
            Log.i("saveCake","saving cake")
            val reference = getDatabaseRef()?.push()
            reference?.updateChildren(c.toMap())?.addOnCompleteListener { task ->
                if (task.isComplete) {
                    onComplete?.onComplete(task)
                    setChanged()
                    notifyObservers()
                }
            }

        }
}