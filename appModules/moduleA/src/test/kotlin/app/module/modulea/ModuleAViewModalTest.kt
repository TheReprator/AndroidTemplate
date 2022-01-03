package app.module.modulea

import app.module.modulea.util.CoroutinesTestExtension
import app.module.modulea.util.InstantExecutorExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExtendWith(value = [InstantExecutorExtension::class])
class ModuleAViewModalTest {

    @JvmField
    @RegisterExtension
    val coroutinesTestRule = CoroutinesTestExtension()

}