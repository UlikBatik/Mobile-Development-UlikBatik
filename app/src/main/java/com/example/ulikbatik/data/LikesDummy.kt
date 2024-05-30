package com.example.ulikbatik.data

import com.example.ulikbatik.R

data class Likes(
    val imageRes: Int,
    val username: String,
    val description: String
)

class LikesDummy {
    companion object {
        fun getLikes(): List<Likes> {
            val dummyData = listOf(
                R.drawable.img_batik1,
                R.drawable.img_batik2,
                R.drawable.img_batik3,
                R.drawable.img_batik4,
                R.drawable.img_batik5,
                R.drawable.img_batik6
            )

            return dummyData.mapIndexed { index, imageRes ->
                Likes(
                    imageRes,
                    "user${index + 1}",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                )
            }
        }
    }
}