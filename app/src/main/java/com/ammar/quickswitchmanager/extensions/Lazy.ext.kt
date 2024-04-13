package com.ammar.quickswitchmanager.extensions

import androidx.compose.foundation.lazy.LazyListState

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
