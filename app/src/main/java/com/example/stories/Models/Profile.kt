package com.example.stories.Models

class Profile(
    val name: String,
    val profilePicture: String,
    val stories: ArrayList<Story>,
    val LastSeenStory: Int = 0
)