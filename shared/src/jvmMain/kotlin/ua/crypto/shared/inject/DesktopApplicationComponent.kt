package ua.crypto.shared.inject

import me.tatarka.inject.annotations.Component
import ua.crypto.core.inject.ApplicationScope

@Component
@ApplicationScope
abstract class DesktopApplicationComponent :
    SharedApplicationComponent/*,
  QaApplicationComponent*/ {

    companion object
}
