package com.example.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.model.UsersListener
import com.example.recyclerview.model.UsersService

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var adapter: UsersAdapter

  /*
  Добавили Геттер
  Чтобы получать доступ к нашей модели к классу UsersService
   */
  private val usersService: UsersService
    get() = (applicationContext as App).usersService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    adapter = UsersAdapter() // создали алаптер
    val layoutManager = LinearLayoutManager(this) // инициализировали как будут отображаться наш список (вертикально или горизонтально)
    binding.recyclerView.layoutManager = layoutManager // назначали для нашего  recyclerView layoutManager который LinearLayoutManager
    binding.recyclerView.adapter = adapter // назначали для нашего  recyclerView adapter который мы созздали

    usersService.addListener(usersListener)
  }
  // Добавили слушателя который будет прзослушивать изминения в классе UsersService
  // здесь в качестве аргумента приходит обновленный список List<users>
  private val usersListener: UsersListener = {
    adapter.users = it // для этого на адаптере в users присвоить новый список который пришел в лямбде через оператор it

  }

  override fun onDestroy() {
    super.onDestroy()
    usersService.removeListener(usersListener)
  }
}