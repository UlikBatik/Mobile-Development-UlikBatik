package com.example.ulikbatik.data

import com.example.ulikbatik.R

data class Post(
    val imageRes: Int,
    val username: String
)

class PostDummy {

    companion object {
        fun getPosts(): List<Post> {
            return listOf(
                Post(R.drawable.img_batik1, "User1"),
                Post(R.drawable.img_batik2, "User2"),
                Post(R.drawable.img_batik3, "User3"),
                Post(R.drawable.img_batik4, "User4"),
                Post(R.drawable.img_batik5, "User5")
            )
        }
    }
}
