package com.example.superheroes.activitis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.superheroes.R

class SuperheroDetalle : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "SUPERHERO_ID"
        const val EXTRA_NAME = "SUPERHERO_NAME"
        const val EXTRA_IMAGE = "SUPERHERO_IMAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_superhero_detalle)




    }
}