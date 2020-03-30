package pl.piotrdev.fastservant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_nowe_zamowienie.*
import kotlinx.android.synthetic.main.content_note.*
import kotlinx.android.synthetic.main.content_note.view.*
import kotlinx.android.synthetic.main.content_note.view.noteET

import kotlinx.android.synthetic.main.content_zamowienia.*
import kotlinx.android.synthetic.main.content_zamowienia.view.*
import kotlinx.android.synthetic.main.delete_alert.view.*

class ZamowieniaActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var snapshotRef: DataSnapshot
    lateinit var zamowieniaRV: RecyclerView
    //var numerstolika: String = "stolik 1"
    var suma:Double = 0.0
    var chwytak:String = ""
    lateinit var ref4: DatabaseReference
    lateinit var sumaTV:TextView
    lateinit var uwagaTV:TextView
    var numerspinner: Int = 0


    var uwaga:String = ""
    var firebaseDat = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zamowienia)
        var numerstolika: String = "stolik 1"



        if(intent.hasExtra("numer_stolikaX")) numerstolika = intent.getSerializableExtra("numer_stolikaX").toString()


if(numerstolika == "stolik 1") numerspinner = 0
if(numerstolika == "stolik 2") numerspinner = 1
if(numerstolika == "stolik 3") numerspinner = 2
if(numerstolika == "stolik 4") numerspinner = 3
if(numerstolika == "stolik 5") numerspinner = 4
if(numerstolika == "stolik 6") numerspinner = 5
if(numerstolika == "stolik 7") numerspinner = 6



        val tables = arrayOf(

            "stolik 1",
            "stolik 2",
            "stolik 3",
            "stolik 4",
            "stolik 5",
            "stolik 6",
            "stolik 7"

        )
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, tables)
        zamowieniaRV = findViewById(R.id.zamowieniaRV)

        zamowieniaRV.layoutManager = GridLayoutManager(applicationContext, 1) as RecyclerView.LayoutManager?


        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner3.adapter = adapter2
        spinner3.setSelection(numerspinner)




        ref = FirebaseDatabase.getInstance().getReference(numerstolika)
        ref4 = FirebaseDatabase.getInstance().getReference("stolik 1")
        sumaTV = findViewById(R.id.suma_TV)






        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                suma = 0.0
                sumaTV.text = suma.toString()

                var numerstolika = parent.getItemAtPosition(position).toString()
                if( chwytak.isNotEmpty()){
                        numerstolika = chwytak
                    }
                ref = FirebaseDatabase.getInstance().getReference(numerstolika)
                    val option = FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(ref, Model::class.java)
                        .setLifecycleOwner(this@ZamowieniaActivity)
                        .build()

                    val firebaseRecyclerAdapter =
                        object : FirebaseRecyclerAdapter<Model, MyViewHolder>(option) {

                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): MyViewHolder {
                                val itemView = LayoutInflater.from(applicationContext)
                                    .inflate(R.layout.produkty_card_view, parent, false)




                                return MyViewHolder(itemView)
                            }

                        override fun onBindViewHolder(
                            holder: MyViewHolder,
                            position: Int,
                            model: Model
                        ) {
                            val placeid = getRef(position).key.toString()


                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Wystąpił błąd " + p0.toException(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    holder.textViewName.text = model.name
                                    holder.textViewPrice.text = model.price + " zł"
                                    if(model.uwaga.isNullOrEmpty()) {
                                        holder.uwagaTV.text = ""
                                    }else {
                                        holder.uwagaTV.text = model.uwaga
                                    }

                                }
                            })

                            holder.deletebutton.setOnLongClickListener {


                                val a:String
                                val b:String
                                val c:String
                                a = model.name.toString()
                                b = model.price.toString()
                                c = model.id.toString()

                                ref.child(c).setValue(null)
                                suma-=model.price!!.toDouble()
                                var  az = String.format("%.2f", suma)
                                sumaTV.text = az+" zł"

                                Toast.makeText(
                                    applicationContext,
                                    "Usunięto produkt: \n"+a+"\nz listy zamówień stolika:\n"+numerstolika,
                                    Toast.LENGTH_LONG
                                ).show()
                                true
                            }
                            var dodajnik:Double = 0.0
                            dodajnik = model.price!!.toDouble()
                            suma==0.0
                            sumaTV.text = (suma).toString()+" zł"
                            suma+=dodajnik
                            var az = String.format("%.2f", suma)


                            sumaTV.text = az+" zł"

                            holder.itemView.setOnLongClickListener{



                                val mDialogView = LayoutInflater.from(this@ZamowieniaActivity).inflate(R.layout.content_note, null)
                                val mBuilder  = AlertDialog.Builder(this@ZamowieniaActivity)
                                    .setView(mDialogView)
                                    .setTitle("")
                               mDialogView.noteET.setText(model.uwaga)
                                val mAlertDialog  = mBuilder.show()


                                    mDialogView.DodajBT.setOnClickListener{
                                        mAlertDialog.dismiss()
                                        uwaga = mDialogView.noteET.text.toString()

                                        ref.database.getReference(numerstolika).child(placeid).child("uwaga").setValue(uwaga).toString()
                                        suma=suma- model.price!!.toDouble()
                                        sumaTV.text = (suma).toString()+" zł"


                                    }
                                mDialogView.AnulujBT.setOnClickListener {
                                    mAlertDialog.dismiss()
                                }

                                true
                            }

                        }
                    }
                zamowieniaRV.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
                deleteOrderBtn.setOnClickListener {
                    val mDialogView = LayoutInflater.from(this@ZamowieniaActivity).inflate(R.layout.delete_alert, null)
                    val mBuilder  = AlertDialog.Builder(this@ZamowieniaActivity)
                        .setView(mDialogView)
                        .setTitle("")

                    val mAlertDialog  = mBuilder.show()


                    mDialogView.finBtn.setOnClickListener{


                        ref4.database.getReference(numerstolika).setValue(null)
                        suma = 0.0
                        sumaTV.text = (suma).toString()

                        mAlertDialog.dismiss()

                    }
                    mDialogView.anulujBtn.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        suma= 0.0
    }
    override fun onResume() {
        super.onResume()
        suma= 0.0
    }
}

class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {


    internal var deletebutton: Button =
        itemView!!.findViewById<Button>(R.id.delete_button)
    internal var textViewName: TextView =
        itemView!!.findViewById<TextView>(R.id.textViewName)
    internal var textViewPrice: TextView =
        itemView!!.findViewById<TextView>(R.id.textViewPrice)
    internal var uwagaTV:TextView =
        itemView!!.findViewById<TextView>(R.id.uwagaTV)

}



