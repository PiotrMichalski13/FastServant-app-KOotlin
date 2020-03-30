package pl.piotrdev.firebaseanother.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pl.piotrdev.firebaseanother.model.Cake
import pl.piotrdev.firebaseanother.R

class CakeCardAdapter(context: Context, resource: Int, list: ArrayList<Cake>): ArrayAdapter<Cake>(context, resource, list) {

    private var mResource: Int = 0
    private var mList: ArrayList<Cake>
    private var mLayoutInflater: LayoutInflater
    private var mContext: Context = context

    init{
        this.mResource = resource
        this.mList = list
        this.mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val returnView: View?
        if (convertView == null) {
            returnView = try {
                mLayoutInflater.inflate(mResource, null)

            } catch (e: Exception) {
                e.printStackTrace()
                View(context)
            }
            setUI(returnView, position)
            return returnView
        }
        setUI(convertView, position)
        return convertView
    }

    private fun setUI(view: View, position: Int) {
        val cake: Cake? = if(count > position) getItem(position) else null
        val name: TextView? = view.findViewById(R.id.cake_card_name)
        name?.text = cake?.name ?: ""


        val lastName: TextView? = view.findViewById(R.id.cake_card_date_baked)
        lastName?.text = cake?.dateBaked ?: ""


        val skill: TextView? = view.findViewById(R.id.cake_card_expiry_date)
        skill?.text = cake?.expiryDate ?: ""

    }

}