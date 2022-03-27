package com.example.recyclerview

import android.app.Application
import com.example.recyclerview.model.UsersService

class App : Application() {

  // синглтон - из любомого места в нашкм приложении получаем доступ к кклассу 
  val usersService = UsersService()
}