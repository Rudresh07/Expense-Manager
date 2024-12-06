package com.rudy.expensetracker.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe authentication state
    val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    var inProcess = mutableStateOf(false)

    init {
        checkAuthStatus()
    }

    // Check if the user is already authenticated
    private fun checkAuthStatus() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Handle login logic
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all the credentials before login")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Handle sign-up logic
    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all the credentials before signing up")
            return
        }

        inProcess.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    inProcess.value = false
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                    inProcess.value = false
                }
            }
    }

    // Handle logout logic
    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

// Authentication states for UI handling
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
