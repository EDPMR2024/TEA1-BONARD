package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    private lateinit var editTextPseudo: EditText
    private lateinit var buttonOk: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPseudo = findViewById(R.id.editTextPseudo)
        buttonOk = findViewById(R.id.buttonOk)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Récupérer le dernier pseudo saisi
        val lastPseudo = sharedPreferences.getString("LAST_PSEUDO", "")
        editTextPseudo.setText(lastPseudo)

        buttonOk.setOnClickListener {
            try {
                val pseudo = editTextPseudo.text.toString()
                Log.d("MainActivity", "Pseudo entered: $pseudo")
                // Sauvegarder le pseudo dans les préférences
                sharedPreferences.edit().putString("LAST_PSEUDO", pseudo).apply()
                // Lancer ChoixListActivity
                val intent = Intent(this, ChoixListActivity::class.java)
                intent.putExtra("PSEUDO", pseudo)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error on button click", e)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_preferences -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
