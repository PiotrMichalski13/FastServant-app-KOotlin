package pl.piotrdev.fastservant

import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_nowe_zamowienie.*

import kotlinx.android.synthetic.main.activity_opcje.*
import kotlinx.android.synthetic.main.activity_produkty.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_opcje.*
import kotlinx.android.synthetic.main.content_zamowienia.*
import kotlinx.android.synthetic.main.edit_product.*
import kotlinx.android.synthetic.main.edit_product.view.*
import kotlinx.android.synthetic.main.edit_product.view.nameEditET
import kotlinx.android.synthetic.main.rec_layout.*
import kotlinx.android.synthetic.main.rec_layout.view.*
import kotlinx.android.synthetic.main.rec_layout.view.nazwa_produktuTV
import java.text.FieldPosition
import kotlin.random.Random

class NoweZamowienieActivity : AppCompatActivity() {



    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    lateinit var mrecylerview : RecyclerView
    lateinit var tekstwiu:TextView
    lateinit var tekstwiu2:TextView
    lateinit var gotoorder:Button
    var numer_stolika:String = "stolik 01"
    var  id: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcje)

        ref = FirebaseDatabase.getInstance().getReference("produkty")
        ref2 =  FirebaseDatabase.getInstance().getReference(numer_stolika)
        mrecylerview = findViewById(R.id.mrecycler)
        tekstwiu = findViewById(R.id.tekstwiu)
        tekstwiu2 = findViewById(R.id.tekstwiu2)
        gotoorder = findViewById(R.id.gotoorder)

        firebaseData()
        mrecylerview.layoutManager = GridLayoutManager(applicationContext, 1)
        val stoliki = arrayOf("stolik 1","stolik 2","stolik 3","stolik 4","stolik 5","stolik 6","stolik 7")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            stoliki)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner2.adapter = adapter

        spinner2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                numer_stolika = parent.getItemAtPosition(position).toString()

            }
        }
    }

    fun firebaseData() {


        val option = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(ref, Model::class.java)
            .setLifecycleOwner(this)
            .build()

        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Model, MyViewHolder>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(applicationContext).inflate(R.layout.rec_layout,parent,false)

                return MyViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Model) {
                val placeid = getRef(position).key.toString()


                holder.itemView.setOnClickListener {

                    var id = 1

                    do {
                        intent.putExtra("pobrany",model.name)
                        intent.putExtra("pobrany2",model.price)
                        if (intent.hasExtra("pobrany")) tekstwiu.setText(intent.getStringExtra("pobrany"))
                        if (intent.hasExtra("pobrany2")) tekstwiu2.setText(intent.getStringExtra("pobrany2"))
                        if (intent.hasExtra("pobrany"))saveToOrder()
                    }
                    while (id==50)
                }
                gotoorder.setOnClickListener {
                    intent = Intent(applicationContext, ZamowieniaActivity::class.java)
                    intent.putExtra("numer_stolikaX",numer_stolika)

                    startActivity(intent)
                }

                ref.child(placeid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(applicationContext, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {


                        holder.nazwa_produktuTV.setText(model.name)
                        holder.cenaTV.setText(model.price+" zł")
                    }
                })
            }
        }
        mrecylerview.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()



    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        internal var nazwa_produktuTV: TextView = itemView!!.findViewById<TextView>(R.id.nazwa_produktuTV)
        internal var cenaTV: TextView = itemView!!.findViewById<TextView>(R.id.cenaTV)
    }

    fun saveToOrder() {
        val name = tekstwiu.text.toString().trim()
        val price = tekstwiu2.text.toString().trim()
        val ref2 = FirebaseDatabase.getInstance().getReference(numer_stolika)
        val id = ref2.push().key



        val order = Hero(id!!, name, price)
        Hero(id!!, name, price)
        ref2.child(id!!).setValue(order)

        Toast.makeText(applicationContext, "Dodano produkt do zamówienia", Toast.LENGTH_LONG).show()


    }}




