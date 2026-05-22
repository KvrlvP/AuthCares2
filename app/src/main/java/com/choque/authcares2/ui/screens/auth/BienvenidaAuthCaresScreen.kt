package com.choque.authcares2.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerLow
import com.choque.authcares2.ui.components.OnboardingDots



@Composable
fun BienvenidaAuthCaresScreen(
    heroPainter: Painter,
    modifier: Modifier = Modifier,
    titlePrefix: String = "Bienvenido a",
    brandFirst: String = "Auth",
    brandSecond: String = "Cares",
    subtitle: String = "Tu espacio tranquilo y seguro para el cuidado y apoyo diario.",
    buttonText: String = "Comenzar",
    currentPage: Int = 0,
    pageCount: Int = 3,
    onStartClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurfaceContainerLow
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeroImage(
                painter = heroPainter,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.15f)
            )

            WelcomeContent(
                titlePrefix = titlePrefix,
                brandFirst = brandFirst,
                brandSecond = brandSecond,
                subtitle = subtitle,
                buttonText = buttonText,
                currentPage = currentPage,
                pageCount = pageCount,
                onStartClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.85f)
            )
        }
    }
}

@Composable
private fun HeroImage(
    painter: Painter,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            AuthCaresSurfaceContainerLow
                        )
                    )
                )
        )
    }
}

@Composable
private fun WelcomeContent(
    titlePrefix: String,
    brandFirst: String,
    brandSecond: String,
    subtitle: String,
    buttonText: String,
    currentPage: Int,
    pageCount: Int,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(AuthCaresSurface)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titlePrefix,
            color = AuthCaresOnSurfaceVariant,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = AuthCaresPrimary)) {
                    append(brandFirst)
                }
                withStyle(SpanStyle(color = AuthCaresSecondaryContainer)) {
                    append(brandSecond)
                }
            },
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = subtitle,
            color = AuthCaresOnSurfaceVariant,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(280.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        OnboardingDots(
            currentPage = currentPage,
            pageCount = pageCount,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthCaresPrimary,
                contentColor = AuthCaresOnPrimary
            )
        ) {
            Text(
                text = buttonText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BienvenidaAuthCaresScreenPreview() {
    AuthCares2Theme {
        BienvenidaAuthCaresScreen(
            heroPainter = painterResource(id = R.drawable.hero_bienvenida_authcares)
        )
    }
}

