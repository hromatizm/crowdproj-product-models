import com.benasher44.uuid.uuid4
import com.product.model.*
import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.Nested
import org.slf4j.LoggerFactory
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

@Suppress("unused")
class CassandraTest {

    @Nested
    inner class RepoPmCassandraCreateTest : RepoPmCreateTest() {
        override val repo = PmRepoInitialized(
            initObjects = initObjects,
            repo = repository(uuidNew.asString())
        )
    }

    @Nested
    inner class RepoPmCassandraReadTest : RepoPmReadTest() {
        override val repo = PmRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    @Nested
    inner class RepoPmCassandraUpdateTest : RepoPmUpdateTest() {
        override val repo = PmRepoInitialized(
            initObjects = initObjects,
            repo = repository(lockNew.asString())
        )
    }

    @Nested
    inner class RepoPmCassandraDeleteTest : RepoPmDeleteTest() {
        override val repo = PmRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    @Nested
    inner class RepoPmCassandraSearchTest : RepoPmSearchTest() {
        override val repo = PmRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    companion object {
        private const val CS_SERVICE = "cassandra-1"
        private const val CS_PORT = 9042
        private const val MG_SERVICE = "liquibase-1"

        val LOGGER = LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val resDc = this::class.java.classLoader.getResource("docker-compose-cs.yml")
                ?: throw Exception("No resource found")
            val fileDc = File(resDc.toURI())
            val logConsumer = Slf4jLogConsumer(LOGGER)

            ComposeContainer(fileDc)
                .withExposedService(CS_SERVICE, CS_PORT)
                .withStartupTimeout(Duration.ofMinutes(10))
                .withLogConsumer(CS_SERVICE, logConsumer)
                .withLogConsumer(MG_SERVICE, logConsumer)
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command.*successfully.*", 1)
                )
                .apply { start() }
        }

        fun repository(uuid: String? = null): RepoPmCassandra {
            return RepoPmCassandra(
                keyspaceName = "product_model_ks",
                host = container.getServiceHost(CS_SERVICE, CS_PORT),
                port = container.getServicePort(CS_SERVICE, CS_PORT),
                randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
                dc = "datacenter1",
            ).apply { clear() }
        }

        @JvmStatic
        @AnnotationSpec.AfterClass
        fun finish() {
            container.stop()
        }
    }
}
