package com.example.stories.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stories.Models.Profile
import com.example.stories.Models.Story
import com.example.stories.R
import com.example.stories.databinding.ListItemStoryBarBinding

class ProfileAdapter(val profileList: MutableList<Profile>, val context: Context, val clickInterface:onClick) :
    RecyclerView.Adapter<ProfileAdapter.StoriesProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesProfileViewHolder {
        val viewBinding =
            ListItemStoryBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesProfileViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: StoriesProfileViewHolder, position: Int) {
        holder.setView(profileList[position],position,context)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }


    inner class StoriesProfileViewHolder(viewBinding: ListItemStoryBarBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val profileName : TextView= viewBinding.profileName
        val profileImage :ImageView = viewBinding.profileImage
        fun setView(profile: Profile, position: Int, context: Context) {
            profileName.text = profile.name
            Glide.with(context)
                .load(profile.profilePicture)
                .circleCrop()
                .into(profileImage)
            itemView.setOnClickListener {
                clickInterface.interfaceFun(profile.stories)}
        }
    }


}
interface  onClick{
     fun interfaceFun(list: ArrayList<Story>)
}
