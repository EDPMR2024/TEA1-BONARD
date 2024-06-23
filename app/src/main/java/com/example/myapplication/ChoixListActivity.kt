package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class ChoixListActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var buttonAddList: Button
    private lateinit var pseudo: String
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var userLists: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        listView = findViewById(R.id.listView)
        buttonAddList = findViewById(R.id.buttonAddList)
        pseudo = intent.getStringExtra("PSEUDO") ?: ""
        Log.d("ChoixListActivity", "Pseudo received: $pseudo")

        // Afficher les listes de l'utilisateur
        userLists = getUserLists(pseudo).toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userLists)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("PSEUDO", pseudo)
            intent.putExtra("LIST_INDEX", position)
            startActivity(intent)
        }

        buttonAddList.setOnClickListener {
            // Ajouter une nouvelle liste
            val newListName = "Nouvelle Liste ${userLists.size + 1}"
            addUserList(pseudo, newListName)
            userLists.add(newListName)
            adapter.notifyDataSetChanged()
            Log.d("ChoixListActivity", "Added new list: $newListName")
        }
    }

    private fun getUserLists(pseudo: String): List<String> {
        // Récupérer les listes de l'utilisateur depuis les préférences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lists = sharedPreferences.getStringSet("${pseudo}_LISTS", setOf())
        Log.d("ChoixListActivity", "User lists: $lists")
        return lists?.toList() ?: listOf()
    }

    private fun addUserList(pseudo: String, listName: String) {
        // Ajouter une nouvelle liste dans les préférences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        val lists = sharedPreferences.getStringSet("${pseudo}_LISTS", mutableSetOf()) ?: mutableSetOf()
        lists.add(listName)
        editor.putStringSet("${pseudo}_LISTS", lists)
        editor.apply()
        Log.d("ChoixListActivity", "New list added to preferences: $listName")
    }
}



