package com.example.whatsub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShortestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shortest_distance)

        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val criteria: TextView = findViewById(R.id.textView5)
        val start_st: TextView = findViewById(R.id.start_st)
        val dest_st: TextView = findViewById(R.id.textView7)
        val transStations: TextView = findViewById(R.id.cost_trans_staion)
        val allStations: TextView = findViewById(R.id.cost_all_station)
        val gotomain: Button = findViewById(R.id.button4)

        //결괏값 표시
        val pathArray: IntArray? = intent.getIntArrayExtra("pathArray")
        Log.d("ShortestActivity", "pathArray[0]: ${pathArray?.get(0)}")
        val path: List<Int>? = pathArray?.toList()
        val title = intent.getStringExtra("title")
        var startText = intent.getStringExtra("startText")
        var destText = intent.getStringExtra("destText")

        criteria.setText(title)
        start_st.setText(startText)
        dest_st.setText(destText)

        val route = path?.let { getStationsRoute(it) }
        transStations.setText(route?.get(0))
        allStations.setText(route?.get(1))

        //길찾기 버튼
        findroad.setOnClickListener {
            startText = startstation.getText().toString()
            destText = deststation.getText().toString()
            val startStation = stationMap[startText] ?: -1
            val endStation = stationMap[destText] ?: -1

            if (startStation != -1 && endStation != -1) {
                val intent = Intent(this, SelectActivity::class.java)
                intent.putExtra("startText", startText)
                intent.putExtra("destText", destText)
                startActivity(intent)
            } else
                Toast.makeText(this,"입력한 역 이름이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        gotomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //엔터 또는 다른 작업 시 키보드 숨기기
        startstation.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        //엔터 또는 다른 작업 시 키보드 숨기기
        deststation.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }
    private fun hideKeyboard() {//키보드 숨겨주는 코드
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}