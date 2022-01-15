package app.module.feature.location

import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CompletableDeferred

@AssistedFactory
interface LocationObserverFactory {
    fun create(
        locationStatusListener: CompletableDeferred<LocationStatusListener>
    ): LocationObserver
}