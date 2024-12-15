package ua.hospes.cryptogateway.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController


/**
 * Returns a well typed [ResultBackNavigator] for this [NavGraphBuilderDestinationScope]
 */
@Composable
inline fun <reified D, reified R> resultBackNavigator(
    navController: NavController
): ResultBackNavigator<R> =
    resultBackNavigator(D::class.java, R::class.java, navController)

/**
 * Returns a well typed [ResultRecipient] for this [NavGraphBuilderDestinationScope]
 */
@Composable
inline fun <reified D, reified R> resultRecipient(navBackStackEntry: NavBackStackEntry): ResultRecipient<D, R> =
    resultRecipient(navBackStackEntry, D::class.java, R::class.java)


@Composable
@PublishedApi
internal fun <R> resultBackNavigator(
    destination: Class<*>,
    resultType: Class<R>,
    navController: NavController,
): ResultBackNavigator<R> {

    val backNavigator = remember {
        ResultBackNavigatorImpl(
            navController = navController,
            resultOriginType = destination,
            resultType = resultType
        )
    }

    backNavigator.handleCanceled()

    return backNavigator
}

@Composable
@PublishedApi
internal fun <D, R> resultRecipient(
    navBackStackEntry: NavBackStackEntry,
    originType: Class<D>,
    resultType: Class<R>
): ResultRecipient<D, R> = remember(navBackStackEntry) {
    ResultRecipientImpl(
        navBackStackEntry = navBackStackEntry,
        resultOriginType = originType,
        resultType = resultType,
    )
}


internal fun <D, R> resultKey(
    resultOriginType: Class<D>,
    resultType: Class<R>
) = "whoppah@${resultOriginType.name}@${resultType.name}@result"

internal fun <D, R> canceledKey(
    resultOriginType: Class<D>,
    resultType: Class<R>
) = "whoppah@${resultOriginType.name}@${resultType.name}@canceled"