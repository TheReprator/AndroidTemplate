package app.module.modulea.permission

import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CompletableDeferred

@AssistedFactory
interface LocationObserverFactory {
    fun create(
        locationStatusListener: CompletableDeferred<LocationStatusListener>
    ): LocationObserver
}