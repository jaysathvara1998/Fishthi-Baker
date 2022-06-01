package com.example.fishthibaker.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import java.io.Serializable

data class OrderModel(
    val id: String,
    val houseNo: String,
    val society: String,
    val landmark: String,
    val city: String,
    val state: String,
    val phone: String?,
    val zip: String,
    val productId: String,
    val userId: String,
    var timestamp: Timestamp?,
    val quantity: Int,
    val totalAmount: Int,
    var status: String?,
    var desc: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(houseNo)
        parcel.writeString(society)
        parcel.writeString(landmark)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(phone)
        parcel.writeString(zip)
        parcel.writeString(productId)
        parcel.writeString(userId)
        parcel.writeParcelable(timestamp, flags)
        parcel.writeInt(quantity)
        parcel.writeInt(totalAmount)
        parcel.writeString(status)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderProductModel(
    val product: ProductModel,
    val order: OrderModel
) : Serializable