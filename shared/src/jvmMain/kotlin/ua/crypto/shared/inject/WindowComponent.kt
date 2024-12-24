package ua.crypto.shared.inject

import me.tatarka.inject.annotations.Component
import ua.crypto.core.inject.ActivityScope

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) : SharedUiComponent {

    companion object
}
