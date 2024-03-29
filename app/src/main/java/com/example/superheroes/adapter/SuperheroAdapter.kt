package com.example.superheroes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superheroes.R
import com.example.superheroes.data.Superhero
import com.example.superheroes.databinding.ItemSuperheroBinding
import com.squareup.picasso.Picasso

class SuperheroAdapter(private var items:List<Superhero> = listOf(), val onClickListener:(Int)->Unit) : RecyclerView.Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        val binding = ItemSuperheroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener { onClickListener(position) } // capturamos el click realizada en la imagen
    }

    fun updateItems(results: List<Superhero>?) {
        items = results!!
        notifyDataSetChanged()
    }
}

//ItemSuperhero del fichero que esta en layout y le pone binding  al final
class SuperheroViewHolder(val binding:ItemSuperheroBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(superhero: Superhero) {
        binding.nombretextview.text = superhero.name
        Picasso.get().load(superhero.image.url).into(binding.foto)
    }

}