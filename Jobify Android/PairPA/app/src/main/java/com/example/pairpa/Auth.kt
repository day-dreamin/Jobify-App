package com.example.pairpa

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


var loggedIn = false
lateinit var showEmail: String
lateinit var reviews: List<List<Any>>

//This function populates the reviews with current hardcoded reviews and reviews from the Firebase database
fun populateReviews() {
    //populate reviews
    val dbReference: DatabaseReference = database.getReference("Reviews")
    reviews = listOf(
        listOf("Jake", "very nice", 4.0f, ""),
        listOf("Jim", "Amazing!!!", 5.0f, ""),
        listOf("John smith", "not the best", 3.0f, ""),
        listOf("htet wai yan", "no. just no", 1.0f, ""),
        listOf("basi saskaran", "why no discount", 4.0f, ""),
        listOf("johndoe", "mid", 2.0f, "")
    )
    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (reviewSnapshot in dataSnapshot.children) {
                val review = reviewSnapshot.getValue(ReviewData::class.java)
                if (review != null) {

                    //review3 = review3 + reviews
                    val review2 = listOf(listOf(review.dispName, review.content, review.rating))
                    reviews = reviews + review2
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Error while iterating through reviews", databaseError.toException())
        }
    })
}

//This function is called when the Submit Review button is pressed, allowing the user to submit and upload a review
//to the Firebase database
fun submitReview(stars: Float, text: String){
    //toast if failure
    if (!loggedIn) {
        Toast.makeText(ctx, "Not logged in!", Toast.LENGTH_SHORT).show()
    }
    else {
        Toast.makeText(ctx, "Review submitted!", Toast.LENGTH_SHORT).show()

        val dbReference: DatabaseReference = database.getReference("Reviews")
        val newReview = ReviewData(getDispName(auth.currentUser?.email.toString()), text, stars, false)
        val userId: String? = auth.currentUser?.uid

        if (userId != null) {
            dbReference.push().setValue(newReview).addOnSuccessListener {
                populateReviews()
            }.addOnFailureListener {
            }
        }

    }
}

//This function is called when the User tried to log in, authenticating the user through Firebase Authentication
fun login(email: String, password: String): Boolean {
    var isSuccessful2 = false
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                loggedIn = true
                isSuccessful2 = true

                val user = auth.currentUser
                showEmail = user?.email.toString()

                val showName = auth.currentUser?.email.toString()
                val showName2 = getDispName(showName)
                Toast.makeText(ctx, "Welcome back, $showName2", Toast.LENGTH_SHORT).show()
                navController.navigate("main")
            }
            else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    ctx,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    if (isSuccessful2) {
        return true
    }
    return false
}

//This function is called when the user tried to sign up for a new account, checking if the email and password is valid,
//and adding the User with initial data into the database
fun signup(email: String, password: String): Boolean {
    //same deal as login
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener() { task ->
            if (task.isSuccessful) {

                loggedIn = true
                val user = auth.currentUser
                showEmail = user?.email.toString()

                val dbReference: DatabaseReference = database.getReference("Users")
                val userId: String = user!!.uid
                val arrayList1 = ArrayList<CartData>()
                val arrayList2 = ArrayList<CartData>()

                val userInfo = User(user?.email.toString(), arrayList1, arrayList2, "Not set", "Not set", "Not set", "Not set")

                if (userId != null) {
                    dbReference.child(userId).setValue(userInfo).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
                }

                Toast.makeText(ctx, "Registration success!", Toast.LENGTH_SHORT).show()
                navController.navigate("main")
            }
            else {
                if (task.exception is FirebaseAuthUserCollisionException) {
                    Toast.makeText(ctx, "User with this email already exists!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (password.length < 6) {
                        Toast.makeText(ctx, "Password must be more than 5 characters!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(ctx, "Invalid email!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    return true
}

//This function obtains the user's Display Name based on the User's email
fun getDispName(email: String): String {
    val split = email.split("@")
    val displayName1 = split[0]
    return displayName1
}


