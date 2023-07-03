package com.bupware.wedraw.android.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//region MainScreen
//TODO gestionar inputs y esas cosas
@Composable
fun TextFieldJoin(modificador: Modifier){

    Column(modifier = modificador, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = "aa",
            onValueChange = {},
            maxLines = 1,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.LightGray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            //TODO CAMBIAR EL COLOR DE LA LINEA CUANDO ESCRIBES
            )
        )
    }

}
//endregion