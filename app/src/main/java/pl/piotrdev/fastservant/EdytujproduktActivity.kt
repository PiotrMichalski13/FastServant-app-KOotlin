package pl.piotrdev.fastservant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_usun_produkt.*
import kotlinx.android.synthetic.main.content_zamowienia.*
import kotlinx.android.synthetic.main.edit_product.view.*

class EdytujproduktActivity : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var zamowieniaRV2: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usun_produkt)

        ref = FirebaseDatabase.getInstance().getReference("produkty")
        zamowieniaRV2 = findViewById(R.id.zamowieniaRV2)
        zamowieniaRV2.layoutManager = GridLayoutManager(applicationContext, 1)

                val option = FirebaseRecyclerOptions.Builder<Model>()
                    .setQuery(ref, Model::class.java)
                    .setLifecycleOwner(this@EdytujproduktActivity)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Model, MyViewHolder2>(option) {

                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): MyViewHolder2 {
                            val itemView = LayoutInflater.from(applicationContext)
                                .inflate(R.layout.produkty_card_view, parent, false)
                            return MyViewHolder2(itemView)
                        }
                        override fun onBindViewHolder(
                            holder: MyViewHolder2,
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
                                    holder.textViewName.setText(model.name)
                                    holder.textViewPrice.setText(model.price + " zł")
                                }
                            })

                                holder.deletebutton.setOnLongClickListener {
                                    val a:String
                                    val b:String
                                    val c: String
                                    a = model.name.toString()
                                    b = model.price.toString()
                                    c = model.id.toString()
                                    ref.child(c).setValue(null)

                                    Toast.makeText(
                                        applicationContext,
                                        "Usunięto produkt: \n"+a+"\nz bazy danych",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    true
                            }
                            holder.itemView.setOnLongClickListener {

                                val mDialogView = LayoutInflater.from(this@EdytujproduktActivity).inflate(R.layout.edit_product, null)
                                val mBuilder  = AlertDialog.Builder(this@EdytujproduktActivity)
                                    .setView(mDialogView)
                                    .setTitle("Edytuj produkt:")
                                mDialogView.nameEditET.setText(model.name)
                                mDialogView.priceEditET.setText(model.price)
                                val mAlertDialog  = mBuilder.show()

                                mDialogView.Dodaj2BT.setOnClickListener {

                                        mAlertDialog.dismiss()
                                        val c:String
                                        val name = mDialogView.nameEditET.text.toString()
                                        val price = mDialogView.priceEditET.text.toString().replace(',','.')
                                        val id = ref.push().key
                                        c = model.id.toString()

                                        ref.child(c).setValue(null)

                                        val order = Hero(id!!, name, price)
                                        Hero(id!!, name, price)
                                        ref.child(id!!).setValue(order)
                                }
                                mDialogView.Anuluj2BT.setOnClickListener {
                                    mAlertDialog.dismiss()
                                }
                                true
                            }
                        }
                    }
                zamowieniaRV2.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }
        }

class MyViewHolder2(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        internal var deletebutton: Button =
            itemView!!.findViewById<Button>(R.id.delete_button)
        internal var textViewName: TextView =
            itemView!!.findViewById<TextView>(R.id.textViewName)
        internal var textViewPrice: TextView =
            itemView!!.findViewById<TextView>(R.id.textViewPrice)
}
