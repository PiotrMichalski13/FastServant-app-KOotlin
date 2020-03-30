package pl.piotrdev.firebaseanother

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import pl.piotrdev.firebaseanother.adapters.CakeCardAdapter
import pl.piotrdev.firebaseanother.model.Cake
import pl.piotrdev.firebaseanother.model.CakeModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), Observer {

    private var mCakeListAdapter: CakeCardAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CakeModel

        val dataList: ListView = findViewById(R.id.person_list)


        val data:ArrayList<Cake> = ArrayList()
        mCakeListAdapter = CakeCardAdapter(
            this,
            R.layout.cake_card_item,
            data
        )
        dataList.adapter = mCakeListAdapter

        add_cake.setOnClickListener {
            addCake()
        }

    }

    private fun addCake() {
        val intent = Intent(this, AddCakeActivity::class.java)
        startActivity(intent)
    }

    override fun update(p0: Observable?, p1: Any?) {
        refresh()
    }

    private fun refresh() {
        mCakeListAdapter?.clear()

        val data = CakeModel.getData()
        if (data != null) {
            mCakeListAdapter?.clear()
            mCakeListAdapter?.addAll(data)
            mCakeListAdapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        CakeModel.addObserver(this)
        refresh()
    }


    override fun onStop() {
        super.onStop()
        CakeModel.deleteObserver(this)

    }

    override fun onPause() {
        super.onPause()
        CakeModel.deleteObserver(this)
    }


}