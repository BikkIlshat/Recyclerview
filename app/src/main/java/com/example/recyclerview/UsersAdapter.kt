package com.example.recyclerview

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview.databinding.ItemUserBinding
import com.example.recyclerview.model.User

interface UserActionListener {
  fun onUserMove(user: User, moveBy: Int)
  fun onUserDelete(user: User)
  fun onUserDetails(user: User)
  fun onUserFire(user: User)
}

class UsersDiffCallback(
  private val oldList: List<User>,
  private val newList: List<User>,
) : DiffUtil.Callback() {
  override fun getOldListSize(): Int = oldList.size
  override fun getNewListSize(): Int = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldUser = oldList[oldItemPosition]     // oldItemPosition - индекс элемента старого списка
    val newUser = newList[newItemPosition]     // newItemPosition - индекс элемента нового списка
    return oldUser.id == newUser.id
  }

  // Сравнивает содержимое элеменов
  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldUser = oldList[oldItemPosition]     // oldItemPosition - индекс элемента старого списка
    val newUser = newList[newItemPosition]     // newItemPosition - индекс элемента нового списка
    return oldUser == newUser
  }

}


class UsersAdapter(
  private val actionListener: UserActionListener,
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

  var users: List<User> = emptyList()
    set(newValue) {
      // берем старывый список и посчитаем разницу
      val diffUtilCallback = UsersDiffCallback(field, newValue)
      val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
      field = newValue
      diffResult.dispatchUpdatesTo(this)
    }

  override fun onClick(view: View) {
    val user = view.tag as User
    when (view.id) {
      R.id.moreImageViewButton -> {
        showPopupMenu(view)
      }
      else -> {
        actionListener.onUserDetails(user)
      }
    }
  }

  override fun getItemCount(): Int = users.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ItemUserBinding.inflate(inflater, parent, false)

    binding.root.setOnClickListener(this)
    binding.moreImageViewButton.setOnClickListener(this)
    return UsersViewHolder(binding)
  }

  override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
    val user = users[position]
    val context = holder.itemView.context
    with(holder.binding) {
      holder.itemView.tag = user
      moreImageViewButton.tag = user
      userNameTextView.text = user.name
      userCompanyTextView.text = if (user.company.isNotBlank())
        user.company else context.getString(R.string.unemployed)
      if (user.photo.isNotBlank()) {
        Glide.with(photoImageView.context)
          .load(user.photo)
          .circleCrop()
          .placeholder(R.drawable.ic_user_avatar)
          .error(R.drawable.ic_user_avatar)
          .into(photoImageView)
      } else {
        Glide.with(photoImageView.context).clear(photoImageView)
        photoImageView.setImageResource(R.drawable.ic_user_avatar)
      }
    }
  }

  private fun showPopupMenu(view: View) {
    val popupMenu = PopupMenu(view.context, view) // создали сам объект PopupMenu
    val context = view.context
    val user = view.tag as User
    val position = users.indexOfFirst { it.id == user.id }

    popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
      isEnabled = position > 0
    }
    popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
      isEnabled = position < users.size - 1
    }
    popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))
    if (user.company.isNotBlank()) {
      popupMenu.menu.add(0, ID_FIRE, Menu.NONE, context.getString(R.string.fire))
    }
    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        ID_MOVE_UP -> {
          actionListener.onUserMove(user, -1)
        }
        ID_MOVE_DOWN -> {
          actionListener.onUserMove(user, 1)
        }
        ID_REMOVE -> {
          actionListener.onUserDelete(user)
        }
        ID_FIRE -> {
          actionListener.onUserFire(user)
        }
      }
      return@setOnMenuItemClickListener true
    }
    popupMenu.show()
  }

  class UsersViewHolder(
    val binding: ItemUserBinding,
  ) : RecyclerView.ViewHolder(binding.root)

  companion object {
    private const val ID_MOVE_UP = 1
    private const val ID_MOVE_DOWN = 2
    private const val ID_REMOVE = 3
    private const val ID_FIRE = 4
  }
}