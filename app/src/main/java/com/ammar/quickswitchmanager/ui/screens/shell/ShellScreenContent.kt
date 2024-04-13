package com.ammar.quickswitchmanager.ui.screens.shell

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammar.quickswitchmanager.extensions.isLastItemVisible
import com.ammar.quickswitchmanager.ui.theme.QuickSwitchManagerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ShellScreenContent(
    modifier: Modifier = Modifier,
    lines: ImmutableList<String> = persistentListOf(),
    showRebootFAB: Boolean = false,
    onRebootClick: () -> Unit = {},
) {
    val state = rememberLazyListState()
    var autoScroll by remember { mutableStateOf(true) }

    LaunchedEffect(lines) {
        if (lines.isEmpty() || !autoScroll) {
            return@LaunchedEffect
        }
        state.animateScrollToItem(lines.size - 1)
    }

    LaunchedEffect(state) {
        snapshotFlow { state.isLastItemVisible }
            .collect { autoScroll = it }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(lines) {
                Text(
                    text = it,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                )
            }
        }
        if (showRebootFAB) {
            RebootButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = (-16).dp,
                        y = (-16).dp,
                    ),
                onClick = onRebootClick,
            )
        }
    }
}

@Composable
private fun RebootButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reboot",
        )
        Spacer(modifier = Modifier.requiredWidth(ButtonDefaults.IconSpacing))
        Text(text = "Reboot")
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewShellScreenContent() {
    QuickSwitchManagerTheme {
        Surface {
            ShellScreenContent(
                lines = List(20) {
                    "test line $it"
                }.toImmutableList(),
            )
        }
    }
}
