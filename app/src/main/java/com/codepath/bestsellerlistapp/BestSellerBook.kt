package com.codepath.bestsellerlistapp

import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single book from the NY Times API
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 *
 * Adding ? next to the datatype allows for null values if late init
 *
 * Serialization is the process of converting data used by an application to a format that can be transferred over a network or stored in a database or a file. In turn, deserialization is the opposite process of reading data from an external source and converting it into a runtime object. Together, they are essential to most applications that exchange data with third parties.
 */
class BestSellerBook {
    @SerializedName("rank")
    var rank = 0

    @JvmField
    @SerializedName("title")
    var title: String? = null

    @JvmField
    @SerializedName("author")
    var author: String? = null

    //TODO bookImageUrl
    @SerializedName("book_image")
    var bookImageUrl : String? = null

    //TODO description
    @SerializedName("description")
    var description : String? = null

    //TODO-STRETCH-GOALS amazonUrl
    @SerializedName("amazon_product_url")  //the string value within the SerializedName needs to match the corresponding json response category
    var amazonUrl : String? = null
}