package pl.piotrdev.firebaseanother

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener

import kotlinx.android.synthetic.main.activity_add_cake.*
import pl.piotrdev.firebaseanother.model.Cake
import pl.piotrdev.firebaseanother.model.CakeModel

class AddCakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cake)
        CakeModel

        add_cake_save.setOnClickListener { saveCake() }

    }

    private fun saveCake() {
        if (add_cake_name.text == null || add_cake_name.text.length < 0) {
            Toast.makeText(applicationContext, "Please add name", Toast.LENGTH_LONG).show()
            return
        }
        if (add_cake_date_baked.text == null || add_cake_date_baked.text.length < 0) {
            Toast.makeText(applicationContext, "Please add bake date", Toast.LENGTH_LONG).show()

            return
        }
        if (add_cake_expiry_date.text == null || add_cake_expiry_date.text.length < 0) {
            Toast.makeText(applicationContext, "Please add expiry date", Toast.LENGTH_LONG).show()

            return
        }

        val cake = Cake(null)
        cake.name = add_cake_name.text.toString()
        cake.dateBaked = add_cake_date_baked.text.toString()
        cake.expiryDate = add_cake_expiry_date.text.toString()

        CakeModel.saveCake(cake, OnCompleteListener { task ->
            if (task.isComplete) {
                finish()
            }
        })
    }
}
