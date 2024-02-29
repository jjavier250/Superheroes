package com.example.superheroes.activitis
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superheroes.R
import com.example.superheroes.adapter.SuperheroAdapter
import com.example.superheroes.data.Superhero
import com.example.superheroes.data.SuperheroesResponse
import com.example.superheroes.data.SuperheroesServiceApi
import com.example.superheroes.databinding.ActivityMainBinding

import com.example.superheroes.databinding.ItemSuperheroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var binding2: ItemSuperheroBinding

    private lateinit var adapter: SuperheroAdapter
    private var superheroList:List<Superhero> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // definnimos para poder recuperar del xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding2= ItemSuperheroBinding.inflate(layoutInflater)

        //+++++++++++++++++++++++  buscar ++++++++++++++++++++++++++++
        binding.botonbuscar.setOnClickListener {
            val searchText = binding.textobuscar.text.toString()

            searchSuperheroes(searchText)
        }

        adapter = SuperheroAdapter() {
            onItemClickListener(it)
        }

        binding.recyclerview.adapter = adapter


        // 1 columna
       // binding.recyclerview.layoutManager = LinearLayoutManager(this)
        // 2 columnas
        binding.recyclerview.layoutManager = GridLayoutManager(this,2)

        //adapter = SuperheroAdapter(superheroList,{ llamarPantalla(it) })

    }

    // ++++++++   siempre que hay menu se añade  este codigo  +++++++++++++++
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        //llamamos a la funcion para buscar
        initSearchView(menu?.findItem(R.id.menu_buscar))

        return true
    }

    private fun initSearchView(searchItem: MenuItem?) {
        if (searchItem != null) {
            var searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        searchSuperheroes(query)
                    }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
        }
    }


    private fun searchSuperheroes(query: String) {

       binding.progressBar.setVisibility(View.VISIBLE) // reloj de tiempo

        //ocultar teclado

        val imm=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        val retrofit = Retrofit.Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SuperheroesServiceApi = retrofit.create(SuperheroesServiceApi::class.java)


        CoroutineScope(Dispatchers.IO).launch {


            // Llamada en segundo plano

            val response = service.searchByName(query)

            runOnUiThread {
                // Modificar UI
                binding.progressBar.setVisibility(View.GONE) // reloj de tiempo que se oculte

                superheroList = response.body()?.results.orEmpty()
                adapter.updateItems(superheroList)

                if (response != null && superheroList.isNotEmpty()) {
                    Log.i("HTTP", "respuesta correcta :)")
                    // antes estaba así:: adapter.updateItems(response.body()?.results.orEmpty())
                    if(superheroList.isEmpty()){
                        Toast.makeText(this@MainActivity, "NO ENCONTRADO", Toast.LENGTH_SHORT).show();

                    }
                } else {
                        Log.i("HTTP", "respuesta erronea :(")
                    }
                }
            }



    }
    // función para llamar a la apnatlla 2 y pasarle el detalle
    private fun llamarPantalla(position:Int){

        val intent = Intent(this, SuperheroDetalle::class.java)
        intent.putExtra("ID_SUPERHEROE",superheroList[position].id)

        startActivity(intent)


    }


    private fun onItemClickListener(position:Int) {
        val superhero: Superhero = superheroList[position]

        val intent = Intent(this, SuperheroDetalle::class.java)
        intent.putExtra(SuperheroDetalle.EXTRA_ID, superhero.id)
        intent.putExtra(SuperheroDetalle.EXTRA_NAME, superhero.name)
        intent.putExtra(SuperheroDetalle.EXTRA_IMAGE, superhero.image.url)
        startActivity(intent)
        //Toast.makeText(this, getString(horoscope.name), Toast.LENGTH_LONG).show()
    }
}