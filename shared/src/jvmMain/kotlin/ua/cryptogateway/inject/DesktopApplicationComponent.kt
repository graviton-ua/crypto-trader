package ua.cryptogateway.inject

import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class DesktopApplicationComponent :
    SharedApplicationComponent/*,
  QaApplicationComponent*/ {

    companion object
}
