package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.bestsellerlistapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "Tj2q16vK76MxGauVjVhUdKoLuvQAI9KF"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"]= API_KEY
        // Using the client, perform the HTTP request
        client["https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json", params, object :
        JsonHttpResponseHandler() {
            /*
             * The onSuccess function gets called when
             * HTTP response status is "200 OK"
             */
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                // The wait for a response is over
                progressBar.hide() //stop loading animation and display the result

                //determine additional things that happens

                /**
                 * Understanding various log functions (similar to console.log in javascript):
                 *
                 * Log.e: This is for when bad stuff happens. Use this tag in places like inside a catch statement. You know that an error has occurred and therefore you're logging an error.
                 *
                 * Log.w: Use this when you suspect something shady is going on. You may not be completely in full on error mode, but maybe you recovered from some unexpected behavior. Basically, use this to log stuff you didn't expect to happen but isn't necessarily an error. Kind of like a "hey, this happened, and it's weird, we should look into it."
                 *
                 * Log.i: Use this to post useful information to the log. For example: that you have successfully connected to a server. Basically use it to report successes.
                 *
                 * Log.d: Use this for debugging purposes. If you want to print out a bunch of messages so you can log the exact flow of your program, use this. If you want to keep a log of variable values, use this.
                 *
                 * Log.v: Use this when you want to go absolutely nuts with your logging. If for some reason you've decided to log every little thing in a particular part of your app, use the Log.v tag.
                 *
                 *
                 * */

                Log.i("Status code", statusCode.toString())  //convert the status code into a string and print it out
                Log.i("Headers", headers.toString())  //print out information pertaining to the header
                Log.i("Json Response:", json.jsonObject.toString())  //extract the json object and convert it to a string and print it out on the logcat section

                val resultsJson : JSONObject = json.jsonObject.get("results") as JSONObject

                val booksRawJSON : String = resultsJson.get("books").toString()

                //JSON doesn’t only contain Objects - the response can also be an array or a nested object. That's why in this particular response call, we choose to use json.jsonObject.get.

                //TODO - Parse JSON into Models
                val gson = Gson()
                val arrayBookType = object : TypeToken<List<BestSellerBook>>() {}.type

                val models : List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayBookType) // call fromJson using your gson object, json "books" string, and arrayBookType. Save the results to "models"

                //set the Recycler View adapter to the Custom BestSellerBooksRecyclerViewAdapter that has been defined in another kotlin file, which accepts the parameter models and the fragment file, fragments are reusable components.
                recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                // Look for this in Logcat:
                Log.d("BestSellerBooksFragment", "response successful")
            }

            /*
             * The onFailure function gets called when
             * HTTP response status is "4XX" (eg. 401, 403, 404)
             */
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                // The wait for a response is over
                progressBar.hide()

                // If the error is not null, log it!
                t?.message?.let {
                    Log.e("BestSellerBooksFragment", errorResponse)
                }
        }
        }]
        /* Uncomment me once you complete the above sections!
        {
            /*
             * The onSuccess function gets called when
             * HTTP response status is "200 OK"
             */
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                // The wait for a response is over
                progressBar.hide()

                //TODO - Parse JSON into Models

                val models : List<BestSellerBook> = null // Fix me!
                recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                // Look for this in Logcat:
                Log.d("BestSellerBooksFragment", "response successful")
            }

            /*
             * The onFailure function gets called when
             * HTTP response status is "4XX" (eg. 401, 403, 404)
             */
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                // The wait for a response is over
                progressBar.hide()

                // If the error is not null, log it!
                t?.message?.let {
                    Log.e("BestSellerBooksFragment", errorResponse)
                }
            }
        }]
        */

    }

    /*
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }

}
