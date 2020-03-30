package pl.piotrdev.fastservant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {


//


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dodajzamowienieBtn.setOnClickListener {
            intent = Intent(this, NoweZamowienieActivity::class.java)
            startActivity(intent)
        }
        produktBtn.setOnClickListener {
            intent = Intent(this, ProduktyActivity::class.java)
            startActivity(intent)
        }
        zamowienieBtn.setOnClickListener {
            intent = Intent(this, ZamowieniaActivity::class.java)
            startActivity(intent)
        }
        edytujprodukt.setOnClickListener {
            intent = Intent(this, EdytujproduktActivity::class.java)
            startActivity(intent)
        }
    }
}





