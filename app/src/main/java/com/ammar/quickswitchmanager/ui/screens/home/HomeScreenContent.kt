package com.ammar.quickswitchmanager.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ammar.quickswitchmanager.R
import com.ammar.quickswitchmanager.common.PackageDetails
import com.ammar.quickswitchmanager.ui.theme.QuickSwitchManagerTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hasRoot: Boolean = true,
    current: ImmutableList<PackageDetails>? = null,
    others: ImmutableList<PackageDetails>? = null,
    onProviderClick: (PackageDetails) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (hasRoot) {
            ProviderList(
                current = current,
                others = others,
                onProviderClick = onProviderClick,
            )
        } else {
            NoRoot(
                modifier = Modifier.align(Alignment.Center),
            )
        }
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ProviderList(
    modifier: Modifier = Modifier,
    current: ImmutableList<PackageDetails>? = null,
    others: ImmutableList<PackageDetails>? = null,
    onProviderClick: (PackageDetails) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 8.dp,
        ),
    ) {
        current?.let { list ->
            stickyHeader(key = "current") {
                Header("Current recents provider")
            }
            items(list) {
                AppItem(packageDetails = it)
            }
        }
        others?.let { list ->
            stickyHeader(key = "others") {
                Header("Other providers")
            }
            items(list) {
                AppItem(
                    packageDetails = it,
                    onClick = { onProviderClick(it) },
                )
            }
        }
    }
}

@Composable
private fun Header(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(horizontal = 16.dp),
        text = text,
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun AppItem(
    packageDetails: PackageDetails,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier.clickable(
            enabled = onClick != null,
            onClick = onClick ?: {},
        ),
        leadingContent = {
            Image(
                modifier = Modifier.size(40.dp),
                painter = rememberDrawablePainter(packageDetails.icon),
                contentDescription = "Icon for ${packageDetails.packageName}",
            )
        },
        headlineContent = {
            Text(text = packageDetails.label)
        },
        supportingContent = {
            Text(text = packageDetails.packageName)
        },
        trailingContent = if (packageDetails.isCurrentHome) {
            {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                )
            }
        } else null,
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHomeScreenContent() {
    QuickSwitchManagerTheme {
        Surface {
            HomeScreenContent(
                loading = true,
                current = persistentListOf(
                    PackageDetails(
                        packageName = "test.test.test",
                        icon = null,
                        label = "Test",
                        isCurrentRecentsProvider = true,
                        isCurrentHome = true,
                    ),
                ),
                others = persistentListOf(
                    PackageDetails(
                        packageName = "test.test.test",
                        icon = null,
                        label = "Test",
                        isCurrentRecentsProvider = false,
                        isCurrentHome = false,
                    ),
                ),
                hasRoot = true,
            )
        }
    }
}

@Composable
private fun NoRoot(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .alpha(0.8f),
            painter = painterResource(R.drawable.bash),
            contentDescription = "No root",
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "No root access",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNoRoot() {
    QuickSwitchManagerTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                NoRoot(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}
