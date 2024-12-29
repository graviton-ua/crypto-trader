package ua.crypto.ui.common.screens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

@Immutable
interface RailScreen {
    val icon: ImageVector
    val title: StringResource
}