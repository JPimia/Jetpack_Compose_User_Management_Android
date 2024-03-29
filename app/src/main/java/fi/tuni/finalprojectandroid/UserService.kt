package fi.tuni.finalprojectandroid

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Function for fetching users.
 *
 * @param pageSize The number of users to fetch.
 * @param page The page number to fetch.
 * @return The list of fetched users.
 */
suspend fun getUsers(pageSize: Int, page: Int): List<User> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val url = HttpUrl.Builder()
        .scheme("https")
        .host("dummyjson.com")
        .addPathSegment("users")
        .addQueryParameter("limit", pageSize.toString())
        .addQueryParameter("page", page.toString())
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()

    val gson = Gson()
    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
    val jsonArray = jsonObject.getAsJsonArray("users")

    return@withContext gson.fromJson(jsonArray, object : TypeToken<List<User>>() {}.type)
}

/**
 * Function for adding a user.
 *
 * @param user The user to add.
 * @param users The list of current users.
 * @param setUsers The callback function for updating the list of users.
 */
fun AddUser(user: User, users: List<User>, setUsers: (List<User>) -> Unit) {
    val url = "https://dummyjson.com/users"
    val jsonMediaType = "application/json; charset=uft-8".toMediaTypeOrNull()

    val maxId = users.map { it.id.toIntOrNull() ?: 0 }.maxOrNull() ?: 0

    var newId = (maxId + 1).toString()

    val requestBody = """
        {
            "firstName": "${user.firstName}",
            "lastName": "${user.lastName}",
            "age": ${user.age},
            "email": "${user.email}",
            "phone": "${user.phone}"
        }""".trimIndent().toRequestBody(jsonMediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            Log.d("MyTag", users.toString())
            val updatedUsers = users.toMutableList()
            val newUser = user.copy(id = newId)
            updatedUsers.add(newUser)
            setUsers(updatedUsers)
        }

        override fun onFailure(call: Call, e: IOException) {
            Log.d("MyTag", "User added failed")
        }
    })
}

/**
 * Function for deleting a user.
 *
 * @param user The user to delete.
 * @param users The list of current users.
 * @param setUsers The callback function for updating the list of users.
 */
fun deleteUser(user: User, users: List<User>, setUsers: (List<User>) -> Unit) {

    val url = "https://dummyjson.com/users/${user.id}"

    val request = Request.Builder()
        .url(url)
        .delete()
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val updatedUsers = users.toMutableList()
            updatedUsers.remove(user)

            for (i in updatedUsers.indices) {
                updatedUsers[i] = updatedUsers[i].copy(id = (i + 1).toString())
            }
            setUsers(updatedUsers)
        }
        override fun onFailure(call: Call, e: IOException) {
            Log.d("MyTag", "Deleting failed")
        }
    })
}