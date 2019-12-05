package com.example.athens


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import com.example.athens.main.ChooseActivity
import com.example.athens.main.StationActivity
import kotlinx.android.synthetic.main.activity_station.*
import kotlinx.android.synthetic.main.fragment_info.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val manager = (activity as StationActivity).supportFragmentManager
        val addGoodsFragment = AddGoodsFragment()


//        toolbar.inflateMenu(R.menu.toolbar_info)

        toolbar_upbutton.setOnClickListener {
            //之後改掉，fragment返回建
            val intent = Intent(this.context, ChooseActivity::class.java)
            startActivity(intent)
        }
        toolbar_addButton.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.framelayout_station, addGoodsFragment).commit()
        }


    }



}
