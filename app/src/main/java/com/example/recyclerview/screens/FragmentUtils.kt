package com.example.recyclerview.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recyclerview.App


/***
 * Тут создадим фабрику Вьюмодели
 *
 * Задача фабрики Вьюмоделей создавать Вьюмодили причем создавать их с нужными параметрами
 * которые передаются в конструктор
 * Т.к. обычно во вьюмодель передаются в качестве параметра какие-то классы из следующего слоя
 * тоесть из слоя модели то фабрики нужно знать гед искать эти классы модели
 *
 */

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T { // в качестве аргумента приходит класс вьюмодели (modelClass: Class<T>) и в результате должны отдать саму созданную вьюмодель  : T
        val viewModel = when (modelClass) { // передаём modelClass который нам пришел
            UsersListViewModel::class.java -> { // если он равен UsersListViewModel class
                UsersListViewModel(app.usersService) // то мы создаём UsersListViewModel и в конструктор передаём (app.usersService)
            }
            else -> {
                throw IllegalAccessException("Unknown view model class")
            }

        }
        return viewModel as T // потому что нам нужно привести тип к джинерику
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

/*
Создалим Extention метод который мы назовем factory()
и который будет можно вызвать с любого фрагмента
для того что бы каждый раз не писать ViewModelFactory передавать туда private val app: App
это для Лаконичности
 */