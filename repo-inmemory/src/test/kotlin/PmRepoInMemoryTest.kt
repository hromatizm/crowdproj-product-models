import com.product.model.PmRepoInMemory
import com.product.model.PmRepoInitialized
import com.product.model.RepoPmCreateTest
import com.product.model.RepoPmDeleteTest
import com.product.model.RepoPmReadTest
import com.product.model.RepoPmSearchTest
import com.product.model.RepoPmUpdateTest


class PmRepoInMemoryCreateTest : RepoPmCreateTest() {
    override val repo = PmRepoInitialized(
        PmRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class PmRepoInMemoryDeleteTest : RepoPmDeleteTest() {
    override val repo = PmRepoInitialized(
        PmRepoInMemory(),
        initObjects = initObjects,
    )
}

class PmRepoInMemoryReadTest : RepoPmReadTest() {
    override val repo = PmRepoInitialized(
        PmRepoInMemory(),
        initObjects = initObjects,
    )
}

class PmRepoInMemorySearchTest : RepoPmSearchTest() {
    override val repo = PmRepoInitialized(
        PmRepoInMemory(),
        initObjects = initObjects,
    )
}

class PmRepoInMemoryUpdateTest : RepoPmUpdateTest() {
    override val repo = PmRepoInitialized(
        PmRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
