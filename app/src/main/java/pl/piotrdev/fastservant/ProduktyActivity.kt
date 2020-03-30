package pl.piotrdev.fastservant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_produkty.*
import kotlinx.android.synthetic.main.content_main.*

class ProduktyActivity : AppCompatActivity() {
    lateinit var ref1: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produkty)
        ref1 =  FirebaseDatabase.getInstance().getReference("produkty")

        addtobase.setOnClickListener {
            saveproduct()
        }

        button2.setOnClickListener {
            Toast.makeText(this,
                "Kategorie: \n " +
                        "1 - przystawki\n " +
                        "2 - dania głownie\n " +
                        "3 - zupy\n " +
                        "4 - napoje\n " +
                        "5 - alkohol\n " +
                        "aktuanie funkcja nie została włączona",
                Toast.LENGTH_LONG).show()
        }
    }
    fun saveproduct() {


        val name = editTextBase.text.toString().trim()
        val price = priceET.text.toString().trim().replace(',','.')
      //  var scan = priceET.text.toString()
       // val kat = katET.text.toString().trim()

        if (name.isEmpty()) {
            editTextBase.error = "podaj nazwę"
            return
        }else
        if(price.isEmpty()){
            priceET.error = "podaj cenę!"
            return
        }

        val id = ref1.push().key


        val produkt = Hero(id!!, name, price)
        Hero(id, name, price)
        ref1.child(id).setValue(produkt)

        Toast.makeText(applicationContext, "Dodano produkt do bazy", Toast.LENGTH_LONG).show()
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)


    }}



