package lib.dehaat.ledger.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun navigateToInvoiceDetailScreen(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.LedgerInvoiceDetailScreen.screen,
    args = args
)

fun navigateToCreditNoteDetailScreen(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.LedgerCreditNoteDetailScreen.screen,
    args = args
)

fun navigateToPaymentDetailScreen(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.LedgerPaymentDetailScreen.screen,
    args = args
)

fun navigateToOutstandingDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.TotalOutstandingDetailScreen.screen,
    args = args
)

fun navigateToInvoiceListPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.InvoiceListScreen.screen,
    args = args
)

fun navigateToAvailableCreditLimitDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    LedgerRoutes.TotalAvailableCreditLimitScreen.screen,
    args = args
)

fun navigateToRevampInvoiceDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerInvoiceDetailScreen.screen,
    args = args
)

fun navigateToRevampCreditNoteDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerCreditNoteDetailScreen.screen,
    args = args
)

fun navigateToRevampPaymentDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerPaymentDetailScreen.screen,
    args = args
)

fun navigateToRevampWeeklyInterestDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerWeeklyInterestDetailScreen.screen,
    args = args
)

fun navigateToABSDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.ABSDetailScreen.screen,
    args = args
)

fun NavController.navigateTo(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}
