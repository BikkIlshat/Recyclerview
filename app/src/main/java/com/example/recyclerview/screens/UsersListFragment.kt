package com.example.recyclerview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recyclerview.R
import com.example.recyclerview.UserActionListener
import com.example.recyclerview.UsersAdapter
import com.example.recyclerview.databinding.FragmentUsersListBinding
import com.example.recyclerview.model.User

class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private val binding: FragmentUsersListBinding by viewBinding(CreateMethod.INFLATE )
    private lateinit var adapter: UsersAdapter

    // Добавляем поле для доступа Вьюмодэли через делегат by viewModels()
    private val viewModel: UsersListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUsers(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {

            }

            override fun onUserFire(user: User) {
                viewModel.fireUser(user)
            }
        })

        // Подписываемся на данные во воюмодели !!! для фрагмента должен быть viewLifecycleOwner для активити модно просто передать this
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.users = it // в методе адаптер в поле юзерс положить наших пользователей

        } // первый аргумент это объект у которого есть жизненный цикл


        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }

}