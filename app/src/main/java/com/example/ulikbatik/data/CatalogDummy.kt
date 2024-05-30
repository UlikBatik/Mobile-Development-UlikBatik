package com.example.ulikbatik.data

import com.example.ulikbatik.R

data class Catalog (
    val imageRes: Int
)

class CatalogDummy {
    companion object {
        fun getCatalog(): List<Catalog> {
            val dummyData = listOf(
                R.drawable.img_batik1,
                R.drawable.img_batik2,
                R.drawable.img_batik3,
                R.drawable.img_batik4,
                R.drawable.img_batik5,
                R.drawable.img_batik6
            )

            // Create a list of Catalog objects with dummy data
            val catalogList = mutableListOf<Catalog>()
            for (imageRes in dummyData) {
                catalogList.add(Catalog(imageRes))
            }

            return catalogList
        }
    }
}
