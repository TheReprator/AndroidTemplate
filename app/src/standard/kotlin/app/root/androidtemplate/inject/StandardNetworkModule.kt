package app.root.androidtemplate.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import okhttp3.Interceptor

@InstallIn(SingletonComponent::class)
@Module
object StandardNetworkModule {
    /**
     * We have no interceptors in the standard release currently
     */
    @Provides
    @ElementsIntoSet
    fun provideInterceptors(): Set<Interceptor> = emptySet()
}
