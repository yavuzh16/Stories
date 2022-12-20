package com.example.stories

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stories.Adapters.ProfileAdapter
import com.example.stories.Adapters.onClick
import com.example.stories.Fragments.StoriesFragment
import com.example.stories.Models.Profile
import com.example.stories.Models.Story
import com.example.stories.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        val list = createList()
        val adapter = ProfileAdapter(list, this,object :onClick{
            override fun interfaceFun(list: ArrayList<Story>) {
                supportFragmentManager.beginTransaction().replace(binding.fragmentLayout.id,StoriesFragment(list),"storyFragment").commit()
            }
        })
        binding.storiesRecyclerView.adapter = adapter
        binding.storiesRecyclerView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }



    fun createList(): ArrayList<Profile> {
        val storyList = ArrayList<Story>()
        storyList.add(Story("https://cdn.vox-cdn.com/thumbor/0kw6ieKTdzGxVk-ApCKesrwQF2o=/0x0:2257x1320/2000x1333/filters:focal(1129x660:1130x661)/cdn.vox-cdn.com/uploads/chorus_asset/file/6839749/pokemon.0.png"))
        storyList.add(Story("https://assets-prd.ignimgs.com/2022/02/22/pokemonday-1645508695014.png"))
        storyList.add(Story("https://shiftdelete.net/wp-content/uploads/2022/01/pokemon-hunt.jpg"))
        val profileList = ArrayList<Profile>()
        profileList.add(
            Profile(
                "Pikachu",
                "https://www.cartonionline.com/wordpress/wp-content/uploads/2022/10/Pikachu.jpg",
                storyList
            )
        )
        profileList.add(
            Profile(
                "Charizard",
                "https://cdn.pixabay.com/photo/2021/12/26/17/31/pokemon-6895600__340.png",
                storyList
            )
        )
        profileList.add(
            Profile(
                "Squirtle",
                "https://sg.portal-pokemon.com/play/resources/pokedex/img/pm/a3bc17e6215031332462cc64e59b7922ddd14b91.png",
                storyList
            )
        )
        return profileList
    }
}