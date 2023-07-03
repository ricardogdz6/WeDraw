package com.bupware.wedraw.android.ui.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.logic.firebase.FBAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun PreviewLogin(){
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController){
    LoginScreenBody(navController)
}

@Composable
fun LoginScreenBody(navController: NavController,viewModel: LoginViewModel = hiltViewModel()){

    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)

            viewModel.signInWithGoogleCredential(credential){
                Log.i("wawa","EXITO")
            }
        }
        catch (e:Exception) {
            Log.e("Auth", "GoogleSignIn Failed!")
            Log.e("Auth", e.stackTraceToString())
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = {
            val options = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken(context.getString(R.string.firebase_server_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context,options)
            launcher.launch(googleSignInClient.signInIntent)
        }) {
            Text(text = "Sign with Google")
        }

        Button(onClick = { Firebase.auth.signOut() }) {
            Text(text = "Sign out")
        }
        
        Button(onClick = {

            if (FBAuth.user != null) {
                Log.i("wawa",FBAuth.user.email.toString())
            } else Log.i("wawa","NO DATA")
        }) {
            Text(text = "Mostrar data")
        }

    }

}



