package com.product.model.processor

import chain.chain
import chain.rootChain
import chain.worker
import com.product.model.CorSettings
import com.product.model.IProcessor
import com.product.model.InnerPmContext
import com.product.model.general.initStatus
import com.product.model.general.operation
import com.product.model.general.stubs
import com.product.model.inner.InnerPmCommand
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmUserId
import com.product.model.inner.InnerPmWorkMode
import com.product.model.repo.checkLock
import com.product.model.repo.initRepo
import com.product.model.repo.prepareResult
import com.product.model.repo.repoCreate
import com.product.model.repo.repoDelete
import com.product.model.repo.repoPrepareCreate
import com.product.model.repo.repoPrepareDelete
import com.product.model.repo.repoPrepareUpdate
import com.product.model.repo.repoRead
import com.product.model.repo.repoSearch
import com.product.model.repo.repoUpdate
import com.product.model.stubs.*
import com.product.model.validation.*

class PmProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) : IProcessor {

    override suspend fun exec(ctx: InnerPmContext) = businessChain.exec(
        ctx.also { it.corSettings = corSettings }
    )

    private val businessChain = rootChain<InnerPmContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание объявления", InnerPmCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadName("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubValidationBadOwnerId("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в pmValidating") { pmValidating = pmRequest.deepCopy() }
                worker("Очистка id") { pmValidating.id = InnerPmId.NONE }
                worker("Очистка заголовка") { pmValidating.name = pmValidating.name.trim() }
                worker("Очистка описания") { pmValidating.description = pmValidating.description.trim() }
                worker("Очистка id владельца") {
                    pmValidating.ownerId = InnerPmUserId(pmValidating.ownerId.asString().trim())
                }
                validateNameNotEmpty("Проверка, что заголовок не пуст")
                validateNameHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")
                validateOwnerIdNotEmpty("Проверка на непустой id владельца")
                validateOwnerIdProperFormat("Проверка формата id владельца")
                finishPmValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить объявление", InnerPmCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в pmValidating") { pmValidating = pmRequest.deepCopy() }
                worker("Очистка id") { pmValidating.id = InnerPmId(pmValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                finishPmValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение модели продукта из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == InnerPmState.RUNNING }
                    process { pmRepoDone = pmRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить объявление", InnerPmCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadName("Имитация ошибки валидации наименования")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в pmValidating") { pmValidating = pmRequest.deepCopy() }
                worker("Очистка id") { pmValidating.id = InnerPmId(pmValidating.id.asString().trim()) }
                worker("Очистка lock") { pmValidating.lock = InnerPmLock(pmValidating.lock.asString().trim()) }
                worker("Очистка наименования") { pmValidating.name = pmValidating.name.trim() }
                worker("Очистка описания") { pmValidating.description = pmValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateNameNotEmpty("Проверка на непустое наименование")
                validateNameHasContent("Проверка на наличие содержания в наименовании")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")
                finishPmValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение модели продукта из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка модели продукта для обновления")
                repoUpdate("Обновление модели продукта в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить объявление", InnerPmCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в pmValidating") { pmValidating = pmRequest.deepCopy() }
                worker("Очистка id") { pmValidating.id = InnerPmId(pmValidating.id.asString().trim()) }
                worker("Очистка lock") { pmValidating.lock = InnerPmLock(pmValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishPmValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение модели продукта из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление модели продукта из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск объявлений", InnerPmCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adFilterValidating") { pmFilterValidating = pmFilterRequest.deepCopy() }
                validateSearchNameLength("Валидация длины наименования в фильтре")
                validateSearchDescriptionLength("Валидация длины описания в фильтре")
                finishPmFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск модели продукта в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}