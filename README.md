## staffing

Интеграционное приложение для синхронизации информации о департаментах и сотрудниках организации из кадровой системы 
БОСС-Кадровик в облачный портал обчения сотрудников iSpring. 


### 1. Задачи периодической синхронизации
В пакете 'ru.bank.integration.staffing.task' представленны две независимые задачи синхронизации:
 - 'DepartmentsUploadScheduledTasks' - задача синхронизации департаментов (метод departmentsUpload)
 - 'UsersUploadScheduledTasks' - задача синхронизации сотрудников (метод usersUpload)

Во приватных методах реализована вспомогательная логика обработки каждого типа действий с синхронизируемым объектом.

Данные для синхронизации извлекаются из интегационной таблицы СУБД системы БОСС-Кадровик через JPA-репозиторий 
к СУБД БОСС-Кадровик и отправляются через клиент REST-сервиса iSpring.

В процессе обработки используются мапперы модели данных БОСС-Кадровик в модель данных iSpring из пакета 
'ru.bank.integration.staffing.model.mapper'


### 2. JPA-репозиторий к СУБД БОСС-Кадровик
JPA-репозиторий к СУБД БОСС-Кадровик представлен в пакетах:
 - 'ru.bank.integration.staffing.repository' - интерфесы JPA-репозитория
 - 'ru.bank.integration.staffing.model.boss' - модель данных (DB)


### 3. Клиент REST-сервиса iSpring
Клиент REST-сервиса iSpring представлен в пакетах:
 - 'ru.bank.integration.staffing.service' - интерфейсы React сервиса (Spring WebFlux)
 - 'ru.bank.integration.staffing.rest.ispring' - реализация логики отдельных поддержанных REST-вызовов серсиса iSpring
 - 'ru.bank.integration.staffing.model.ispring' - модель данных (JSON)


### 4. Конфигурирование приложения
Конфигурационные параметры по умолчанию для приложения представленны в файле application.yaml

Приложение поддерживает профили и в файлах application-dev.yaml и application-test.yaml приведены примеры конфигураций
для профилей разработки и UAT-тестирования соответственно


### 5. Автотесты

В пакетах 'test.ru.bank.integration.staffing.*' содержатся модульные и интеграционные автотесты (JUnit Jupiter).