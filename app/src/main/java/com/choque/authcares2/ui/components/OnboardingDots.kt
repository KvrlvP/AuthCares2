package com.choque.authcares2.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary

@Composable
fun OnboardingDots(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            val width by animateDpAsState(
                targetValue = if (selected) 32.dp else 8.dp,
                label = "width"
            )

            Box(
                modifier = Modifier
                    .size(
                        width = width,
                        height = 8.dp
                    )
                    .clip(RoundedCornerShape(percent = 50))
                    .background(
                        if (selected) AuthCaresPrimary else AuthCaresOutlineVariant
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingDotsPreview() {
    AuthCares2Theme {
        OnboardingDots(
            currentPage = 0,
            pageCount = 3
        )
    }
}

