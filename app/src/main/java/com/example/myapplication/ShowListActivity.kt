package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class ShowListActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var buttonAddItem: Button
    private lateinit var editTextNewItem: EditText
    private lateinit var pseudo: String
    private var listIndex: Int = -1
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listItems: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        listView = findViewById(R.id.listView)
        buttonAddItem = findViewById(R.id.buttonAddItem)
        editTextNewItem = findViewById(R.id.editTextNewItem)
        pseudo = intent.getStringExtra("PSEUDO") ?: ""
        listIndex = intent.getIntExtra("LIST_INDEX", -1)
        Log.d("ShowListActivity", "Pseudo received: $pseudo, List index: $listIndex")

        // Récupérer les éléments de la liste
        listItems = getListItems(pseudo, listIndex).toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listItems)
        listView.adapter = adapter

        buttonAddItem.setOnClickListener {
            val newItem = editTextNewItem.text.toString()
            if (newItem.isNotEmpty()) {
                addListItem(pseudo, listIndex, newItem)
                listItems.add(newItem)
                adapter.notifyDataSetChanged()
                editTextNewItem.text.clear()
                Log.d("ShowListActivity", "Added new item: $newItem")
            }
        }
    }

    private fun getListItems(pseudo: String, listIndex: Int): List<String> {
        // Récupérer les éléments de la liste depuis les préférences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lists = sharedPreferences.getStringSet("${pseudo}_LISTS_$listIndex", setOf())
        Log.d("ShowListActivity", "List items: $lists")
        return lists?.toList() ?: listOf()
    }

    private fun addListItem(pseudo: String, listIndex: Int, item: String) {
        // Ajouter un nouvel élément dans la liste des préférences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        val listItems = sharedPreferences.getStringSet("${pseudo}_LISTS_$listIndex", mutableSetOf()) ?: mutableSetOf()
        listItems.add(item)
        editor.putStringSet("${pseudo}_LISTS_$listIndex", listItems)
        editor.apply()
        Log.d("ShowListActivity", "New item added to list: $item")
    }
}


