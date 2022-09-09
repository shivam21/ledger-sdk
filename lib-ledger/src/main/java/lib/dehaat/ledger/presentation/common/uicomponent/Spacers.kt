package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpaceMedium() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SpaceSmall12() = Spacer(modifier = Modifier.height(12.dp))

@Composable
fun HorizontalSpacer(width: Dp) = Spacer(modifier = Modifier.width(width))

@Composable
fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))
