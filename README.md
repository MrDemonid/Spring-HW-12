## Паттерны и их обоснование.

### Payment.
Используется паттерн `Chain of Responsibility`, позволяющий выстраивать
цепочки нужных операций.

Платежный интерфейс `PaymentContext` представлен паттерном `Strategy`, для удобного
переключения типов оплаты (кредитная карта, Pay Pal, Быстрый платеж и тд) на лету.

Это позволяет работать с оплатой через единый интерфейс, не зная деталей реализации.



### Catalog.
Использует паттерн `Factory Method`, поскольку этот паттерн обеспечивает легкое 
добавление/удаления новых категорий, без модификации другого кода.

Точнее можно сделать без модификации, если в фабрике вместо switch использовать Map<>.
Но в демонстрационном примере не вижу смысла так заморачиваться.


### Cart.
Используется паттерн `Strategy`. Позволяет легко менять стратегию работы с корзиной
для различных типов пользователей (авторизированные и анонимные).  

Легко добавляются новые стратегии, не затрагивая остальной бизнес-логики. 


### Order.
Используется паттерн `Saga`, поскольку позволяет легко задать не только очередность
выполнения действий, но и их откат в обратном порядке, в случае если на каком-то
действии произойдет ошибка.

Сущность `Order` реализована паттерном `Builder`. В данном случае нужда в нём притянута за уши,
но в качестве демонстрации вполне сойдет. 


## Spring Cloud Bus + RabbitMQ (с Management Plugin)
```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```
Порт 5672 — стандартный порт для AMQP-протокола.

Порт 15672 — для веб-интерфейса RabbitMQ Management.


Админка соответственно: http://localhost:15672

Пароль и логин: `guest`


## Разное.
Поскольку магазин может работать и с не авторизированными пользователями,
то для их идентификации используются куки. Веб-клиент генерирует anon_id
для каждого нового пользователя, пока он не авторизуется. Эти же
идентификационные куки передаются и в корзину, посредством встраивания
их в Feign-запросы, что позволяет корзине правильно идентифицировать и 
создавать отдельную корзину даже для анонимных пользователей.


Генерация приватного ключа для сервера аутентификации:
```shell
openssl genrsa -out private_key.pem 2048
```
Извлечение публичного ключа из приватного:
```shell
openssl rsa -in private_key.pem -pubout -out public_key.pem
```
По необходимости конвертируем первичный ключ из формата PKCS#1 в PKCS#8:
```shell
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_key.pem -out private_key_pkcs8.pem
```

