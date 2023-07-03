package com.bupware.wedraw.android.Login

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.logic.firebase.FBAuth
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.logic.sessionData.sessionData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
@Preview
fun PreviewLogin(){
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController){
    LoginScreenBody(navController)
}

@Composable
fun LoginScreenBody(navController: NavController,viewModel: LoginViewModel = hiltViewModel()){

    //Background
    Column(
        Modifier
        .fillMaxSize()
    ) {
        Image(painter = painterResource(R.drawable.mainbackground), contentDescription = "background", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
    }


    Logo()
    Column(Modifier.padding(top = 230.dp, start = 10.dp, end = 10.dp)) {
        TransparentBackground(navController)
    }

}

@Composable
fun TransparentBackground(navController: NavController){
    Column(
        Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .background(Color.Black.copy(0.4f), RoundedCornerShape(10.dp)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        //TODO CAMBIAR ESTE BOTON DEFINTIVAMENTE
        LogWithGoogle(navController = navController)

    }
}

@Composable
fun Logo(){
    Box(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "background",
            modifier = Modifier
                .size(300.dp)
                .offset(y = (-40).dp)

        )
    }
}

@Composable
fun LogWithGoogle(viewModel: LoginViewModel = hiltViewModel(), navController: NavController){

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            viewModel.signInWithGoogleCredential(credential) {

                navController.navigate(route =Destinations.MainScreen.ruta){navController.popBackStack()}}

                /*
                navController.navigate(
                    route = Destinations.MainScreen.ruta,
                    builder = {
                        popUpTo(route = Destinations.LoginScreen.ruta) {
                            inclusive = true
                        }
                    }
                )

            }

                 */
        } catch (e: Exception) {
            Log.e("Auth", "GoogleSignIn Failed!")
            Log.e("Auth", e.stackTraceToString())
        }
    }

    Button(onClick = {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.firebase_server_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, options)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInClient.signOut() // Agrega esta línea para asegurarte de que se muestre la pantalla de selección de cuenta cada vez
        launcher.launch(signInIntent)
    }) {
        Text(text = "Logear con google")
    }
}



