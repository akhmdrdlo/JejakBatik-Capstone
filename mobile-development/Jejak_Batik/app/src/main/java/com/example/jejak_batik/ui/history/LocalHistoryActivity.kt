package com.example.jejak_batik.ui.history

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jejak_batik.R
import com.example.jejak_batik.room.AppDatabase
import com.example.jejak_batik.room.LocalHistoryDao

class LocalHistoryActivity : AppCompatActivity() {

    private lateinit var adapter: LocalHistoryAdapter
    private lateinit var localHistoryDao: LocalHistoryDao
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_history)
        adapter = LocalHistoryAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        emptyTextView = findViewById(R.id.emptyTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        localHistoryDao = AppDatabase.getDatabase(this).localHistoryDao()

        localHistoryDao.getAllHistory().observe(this) { historyList ->
            if (historyList.isEmpty()) {
                emptyTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateData(historyList)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
