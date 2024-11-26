package ua.cryptogateway.inject

import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) : SharedUiComponent {

    companion object
}
