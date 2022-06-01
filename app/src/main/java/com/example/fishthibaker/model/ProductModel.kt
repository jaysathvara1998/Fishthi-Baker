package com.example.fishthibaker.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

data class ProductModel(
    val category: String,
    val description: String,
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    val weight: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", "")
}

data class CartModel(
    val productRef: DocumentReference,
    val userRef: DocumentReference
)