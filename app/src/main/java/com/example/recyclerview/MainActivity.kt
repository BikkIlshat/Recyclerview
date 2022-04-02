package com.example.recyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.model.User
import com.example.recyclerview.screens.UserDetailsFragment
import com.example.recyclerview.screens.UsersListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer,UsersListFragment())
                .commitAllowingStateLoss()
        }

    }

    override fun showDetails(user: User) {
        //Запускаем фрагмент что бы показать детали пользователя
    supportFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id))
        .commit()
    }

    override fun goBack() {
      onBackPressed() // системный метод назад
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}