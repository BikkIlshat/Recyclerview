package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.UserNotFoundException
import com.example.recyclerview.model.UserDetails
import com.example.recyclerview.model.UsersService

class UserDetailsViewModel(
    private val usersService: UsersService
) : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    /*
    Вызывается сразу при старте фрагмента в методе onCreate
     */
    fun loadUser(userId: Long) {
        // если пользователь уже загружен то мы ничего делать не будем
        if (_userDetails.value != null ) return
        try {
           _userDetails.postValue(usersService.getById(userId))
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
        }
    }

    /*
        На момент вызова deleteUser пользователь будет уже в liveData
        поэтому в параметр идентификатор можем и не передавать
        нам всего лишь нужно проверить загружен ли пользователь если он не загружен
        то мы ничего сделать не сможем Иначе мы его удаляем
  */
    fun deleteUser() {
        val userDetails = this.userDetails.value ?: return
        usersService.deleteUser(userDetails.user)
    }


}