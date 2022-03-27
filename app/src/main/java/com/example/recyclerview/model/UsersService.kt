package com.example.recyclerview.model

import com.github.javafaker.Faker
import java.util.*

/**
 * Класс управления даными (получать данные, удалять, пермещать)
 */
typealias UsersListener = (users: List<User>) -> Unit

class UsersService {

  // создали переменнаую user которач  хранит в себе список пользователей, т.к. нет сети то это просто локальная переменная со списком
  private var users = mutableListOf<User>()

  private val listeners = mutableSetOf<UsersListener>()

  // В блоке init слуайным образом проиницаилизируем пользователей
  init {
    val faker = Faker.instance()
    IMAGES.shuffle() // перемешиваем наши изображения
    users = (1..100).map {
      User(
        id = it.toLong(),
        name = faker.name().name(),
        company = faker.company().name(),
        photo = IMAGES[it % IMAGES.size] // для того чтобы изображения слишком часто не повторялись
      )
    }.toMutableList()
  }

  // методы которые позволят производить некоторые операции с пользователями в списке

  fun getUsers(): List<User> = users

  fun deleteUser(user: User) {
    // т.к. User это data class то удалять можно на прямую
    val indexToDelete = users.indexOfFirst { it.id == user.id }
    if (indexToDelete != -1)
      users.removeAt(indexToDelete)
    notifyChanges()
  }

  /*
  user - >  пользователь которого мы перемещаем
  moveBy: Int - > куда его переместить
  moveBy - если == 1 то мы его перемещаем вверх по индексу в списке (на эмуляторе пользователь будет перемещен вниз)
  moveBy - если == -1 то мы его перемещаем ВНИЗ по индексу в списке (на эмуляторе пользователь будет перемещен ВВЕРХ)

   */

  fun moveUser(user: User, moveBy: Int) {
    val oldIndex = users.indexOfFirst { it.id == user.id } // ищем старый индекс пользователя
    if (oldIndex == -1) return // сравниваем что он не равен 1
    val newIndex =
      oldIndex + moveBy // считаем новый индекс пользователя, который равен старому индексу + значению moveBy
    if (newIndex < 0 || newIndex >= users.size) return // проверяем что новый инлдекс не выходит за границы списка, если выходит делаем return
    Collections.swap(users, oldIndex, newIndex)//  если всё ок меняем элементы местами
/* первый аргумент users -> список в котором будем  менять элементы местамми
 oldIndex, newIndex -> индексы старый и новый  т.к. на новом индексе будет находиться какой то другой элемент списка
 поэтому мы их просто меняем местами
 */
    notifyChanges()
  }

  fun addListener(listener: UsersListener) {
    listeners.add(listener)
    listener.invoke(users)
  }

  fun removeListener(listener: UsersListener) {
    listeners.remove(listener)
  }

  private fun notifyChanges() {
    listeners.forEach { it.invoke(users) }
  }

  companion object {
    private val IMAGES: MutableList<String> = mutableListOf(
      "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
      "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
    )
  }
}

