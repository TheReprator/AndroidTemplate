package app.root.androidtemplate.home

import app.template.base_android.ContentViewSetter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object StandardContentViewModule {
    @Provides
    fun provideContentViewSetter(): ContentViewSetter = ContentViewSetter { activity, view ->
        activity.setContentView(view)
    }
}
