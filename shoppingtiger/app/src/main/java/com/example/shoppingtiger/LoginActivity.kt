package com.example.shoppingtiger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.shoppingtiger.ui.theme.ShoppingTigerTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OptionsManager.initialize(applicationContext)
        FirebaseApp.initializeApp(application)
        auth = FirebaseAuth.getInstance()
        setContent {
            ShoppingTigerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(auth)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(auth: FirebaseAuth, modifier: Modifier = Modifier) {
    var inputTextLogin by remember { mutableStateOf("example@example.com") }
    var inputTextPassword by remember { mutableStateOf("***********") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextLogin,
            onValueChange = {
                inputTextLogin = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextPassword,
            onValueChange = {
                inputTextPassword = it
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(
                    inputTextLogin,
                    inputTextPassword
                ).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(
                            context,
                            "User registered successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            context,
                            "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .requiredWidth(300.dp)
                .requiredHeight(50.dp)
        ){
            Text(
                text = "Register"
            )
        }
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(
                    inputTextLogin,
                    inputTextPassword
                ).addOnCompleteListener{
                    if(it.isSuccessful){
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }else{
                        Toast.makeText(
                            context,
                            "Login failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .requiredWidth(300.dp)
                .requiredHeight(50.dp)
        ){
            Text(
                text = "Log in"
            )
        }
    }
}