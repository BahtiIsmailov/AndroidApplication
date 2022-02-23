package com.example.englishapp.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

private val defaultNavOptions = navOptions {
    anim {
        enter = com.example.englishapp.R.anim.slide_left
        exit = com.example.englishapp.R.anim.wait_anim
        popEnter = com.example.englishapp.R.anim.wait_anim
        popExit = com.example.englishapp.R.anim.slide_right
    }
}

private val emptyNavOptions = navOptions {}

class NavHostFragmentWithDefaultAnimations : NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider.addNavigator(
            // this replaces FragmentNavigator
            FragmentNavigatorWithDefaultAnimations(requireContext(), childFragmentManager, id)
        )
    }

}


@Navigator.Name("fragment")
class FragmentNavigatorWithDefaultAnimations(
    context: Context,
    manager: FragmentManager,
    containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        // this will try to fill in empty animations with defaults when no shared element transitions are set
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#shared-element
        val shouldUseTransitionsInstead = navigatorExtras != null
        val navOptions = if (shouldUseTransitionsInstead) navOptions
        else navOptions.fillEmptyAnimationsWithDefaults()
        return super.navigate(destination, args, navOptions, navigatorExtras)
    }

    private fun NavOptions?.fillEmptyAnimationsWithDefaults(): NavOptions =
        this?.copyNavOptionsWithDefaultAnimations() ?: defaultNavOptions

    private fun NavOptions.copyNavOptionsWithDefaultAnimations(): NavOptions =
        let { originalNavOptions ->
            navOptions {
                launchSingleTop = originalNavOptions.shouldLaunchSingleTop()
                popUpTo(originalNavOptions.popUpTo) {
                    inclusive = originalNavOptions.isPopUpToInclusive
                }
                anim {
                    enter =
                        if (originalNavOptions.enterAnim == emptyNavOptions.enterAnim) defaultNavOptions.enterAnim
                        else originalNavOptions.enterAnim
                    exit =
                        if (originalNavOptions.exitAnim == emptyNavOptions.exitAnim) defaultNavOptions.exitAnim
                        else originalNavOptions.exitAnim
                    popEnter =
                        if (originalNavOptions.popEnterAnim == emptyNavOptions.popEnterAnim) defaultNavOptions.popEnterAnim
                        else originalNavOptions.popEnterAnim
                    popExit =
                        if (originalNavOptions.popExitAnim == emptyNavOptions.popExitAnim) defaultNavOptions.popExitAnim
                        else originalNavOptions.popExitAnim
                }
            }
        }

}