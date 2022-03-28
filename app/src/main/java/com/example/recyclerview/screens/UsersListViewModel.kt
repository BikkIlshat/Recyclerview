package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UsersListener
import com.example.recyclerview.model.UsersService

/***
 * Каждый класс вьюмодели наслудются от ViewModel()
 * В конструктор вьюмодели нужно передать все зависимости от которых она зависит
 *
 * Вьюмодели не должны знать не об Активити не об Фрагменте поэтому они просто содержат LIveData
 * Фрагменты или Активити в свою очередь подписываются на эти LiveData и слушают их изменения
 * LIveData - могут содержать данные которые фрагменту нужно показать (в нашем случае это будет
 * список пользователей) и когда Фрагмет подписывается на LiveData вопервых получает текущее знасение
 * с этой LiveData а так же все последующие обновления
 * LiveData - принято описывать двумя полями:
 * 1 это приватные которые тип MutableLiveData(позыоляет не только слушать данные но и изменять данные внутри себя)
 * 2 View получает доступ к обычной LiveData
 */

class UsersListViewModel(
    private val usersService: UsersService // т.к. вьюмодель имеет конструктор не по умолчанию необходимо будет создавать фабрику Вьюмодели
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>() // изменяемая LiveData для внутреннего использования
    val users: LiveData<List<User>> = _users //  публичнмя переменная отдаём её Вью длч внешнего использования(она не изменяемая LiveData)

/*
Во вьюмодели мы должны описать операции которые разрешено делать из фрагмента
А так же даные которые вьюмодель будет отправлять во фрагмент
В нашем случае это загрузка списка, перемещение пользователя, удаление и запуск экрана с деталями пользователя
*/

    private val listener: UsersListener = {
        _users.postValue(it)
    }

    init {
        loadUsers()
    }

    fun loadUsers() {
        usersService.addListener (listener)
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    fun moveUsers(user: User, moveBy: Int) {
        usersService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User) {
        usersService.deleteUser(user)

    }

    fun fireUser(user: User) {
        usersService.fireUser(user)
    }



}