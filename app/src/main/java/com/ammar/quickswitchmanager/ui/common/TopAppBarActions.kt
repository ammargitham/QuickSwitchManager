package com.ammar.quickswitchmanager.ui.common

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberPlainTooltipPositionProvider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ammar.quickswitchmanager.R

@Composable
fun TopAppBarActions(
    showMoreOptions: Boolean = true,
    showSaveLogsAction: Boolean,
    onChangeDefaultHomeClick: () -> Unit = {},
    onSaveLogsClick: () -> Unit = {},
) {
    if (showSaveLogsAction) {
        SaveLogs(
            onSaveLogsClick = onSaveLogsClick,
        )
    }
    if (showMoreOptions) {
        MoreOptions(
            onChangeDefaultHomeClick = onChangeDefaultHomeClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveLogs(
    modifier: Modifier = Modifier,
    onSaveLogsClick: () -> Unit = {},
) {
    TooltipBox(
        modifier = modifier.wrapContentSize(Alignment.Center),
        positionProvider = rememberPlainTooltipPositionProvider(),
        state = rememberTooltipState(),
        tooltip = { PlainTooltip { Text(text = "Save Logs") } },
    ) {
        IconButton(onClick = onSaveLogsClick) {
            Icon(
                painter = painterResource(R.drawable.baseline_save_24),
                contentDescription = "Save logs",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoreOptions(
    modifier: Modifier = Modifier,
    onChangeDefaultHomeClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val positionProvider = rememberPlainTooltipPositionProvider(
        spacingBetweenTooltipAndAnchor = 8.dp,
    )

    TooltipBox(
        modifier = modifier,
        positionProvider = positionProvider,
        state = rememberTooltipState(),
        tooltip = { PlainTooltip { Text(text = "More") } },
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = scrollState,
        ) {
            DropdownMenuItem(
                text = { Text(text = "Change default home") },
                onClick = onChangeDefaultHomeClick,
            )
        }
    }
}
