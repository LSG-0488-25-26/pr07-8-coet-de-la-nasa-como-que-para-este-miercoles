package com.example.umafacts.view.components

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.umafacts.R
import androidx.core.graphics.toColorInt
import com.example.umafacts.model.UmamusumeDetail


@Composable
fun UmaItem(
    character: UmamusumeDetail,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val mainColor = try {
        Color((character.colorMain ?: "#CCCCCC").toColorInt())
    } catch (_: Exception) {
        Color.Gray
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .background(mainColor.copy(alpha = 0.15f))
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(
                    id = imageFromInternalName(
                        context,
                        character.nameInternal
                    )
                ),
                contentDescription = character.nameEn,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))

            character.nameEn?.let {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@DrawableRes
fun imageFromInternalName(
    context: Context,
    internalName: String?
): Int {
    val resId = context.resources.getIdentifier(
        internalName,
        "drawable",
        context.packageName
    )
    return if (resId != 0) resId else R.drawable.uma_placeholder
}
