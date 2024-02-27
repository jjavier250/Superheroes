package com.example.superheroes.activitis
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superheroes.adapter.SuperheroAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // definnimos para poder recuperar del xml
        binding = ActivityMainBinding.inflate(layoutInflater)
       // binding2= ItemSuperheroBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.botonbuscar.setOnClickListener {
            val searchText = binding.textobuscar.text.toString()

            searchSuperheroes(searchText)
        }

        adapter = SuperheroAdapter()
        binding.recyclerview.adapter = adapter

        // 1 columna
       // binding.recyclerview.layoutManager = LinearLayoutManager(this)
        // 2 columnas
        binding.recyclerview.layoutManager = GridLayoutManager(this,2)

    }

    private fun searchSuperheroes(query: String) {

       binding.progressBar.setVisibility(View.VISIBLE) // reloj de tiempo

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
                if (response != null) {
                    Log.i("HTTP", "respuesta correcta :)")
                    adapter.updateItems(response.body()?.results)
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                }
            }


        }
    }
}