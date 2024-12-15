package ua.cryptogateway.inject

import ua.hospes.cryptogateway.ui.configs.ConfigsComponent
import ua.hospes.cryptogateway.ui.home.HomeComponent
import ua.hospes.cryptogateway.ui.settings.SettingsComponent

interface SharedUiComponent : HomeComponent,
    ConfigsComponent,
    SettingsComponent {
//    val tiviContent: TiviContent
//    val permissionsController: PermissionsController
//
//    @Provides
//    @ActivityScope
//    fun provideLyricist(): TiviStrings = Lyricist(
//        defaultLanguageTag = Locales.EN,
//        translations = Strings,
//    ).strings
//
//    @Provides
//    @ActivityScope
//    fun provideCircuit(
//        uiFactories: Set<Ui.Factory>,
//        presenterFactories: Set<Presenter.Factory>,
//    ): Circuit = Circuit.Builder()
//        .addUiFactories(uiFactories)
//        .addPresenterFactories(presenterFactories)
//        .build()
}
