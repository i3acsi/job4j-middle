## Hibernate

+ [1. Что такое ORM?](#1-Что-такое-ORM)
+ [2. Опиши, как конфигурируется Hibernate. Рассказать про hibernate.cfg.xml и про mapping.](#2-опиши-как-конфигурируется-hibernate-рассказать-про-hibernatecfgxml-и-про-mapping)
+ [3. Жизненный цикл Entiity.](#3-Жизненный-цикл-Entity)
+ [4. Зачем нужен класс SessionFactory? Является ли он потокобезопасным?](#4-Зачем-нужен-класс-SessionFactory-Является-ли-он-потокобезопасным)
+ [5. Зачем нужен класс Session? Является ли он потокобезопасным?](#5-Зачем-нужен-класс-Session-Является-ли-он-потокобезопасным)
+ [6. В чем отличие методов Session.get Session.load?](#6-в-чем-отличие-методов-sessionget-sessionload)
+ [7. Расскажите про методы flush commit.](#7-Расскажите-про-методы-flush-close)
+ [8. В чем отличие методы save от saveOrUpdate и merge?](#8-В-чем-отличие-методы-save-от-saveOrUpdate-и-merge)
+ [9. Расскажите процесс создания, редактирования, чтения и удаления данных через Hibernate.](#9-Расскажите-процесс-создания-редактирования-чтения-и-удаления-данных-через-Hibernate)
+ [10. Как осуществляется иерархия наследования в Hibernate? Рассказать про три стратегии наследования.](#10-Как-осуществляется-иерархия-наследования-в-Hibernate-Рассказать-про-три-стратегии-наследования)
+ [11. Можно ли создать собственный тип данных?](#11-Можно-ли-создать-собственный-тип-данных)
+ [12. Какие коллекции поддерживаются на уровне mapping?](#12-Какие-коллекции-поддерживаются-на-уровне-mapping)
+ [13. Зачем нужен класс Transactional?](#13-Зачем-нужен-класс-Transactional)
+ [14. Расскажите про уровни изоляции? Какие уровни поддерживаются в hibernate? Как их устанавливать?](#14-Расскажите-про-уровни-изоляции-Какие-уровни-поддерживаются-в-hibernate-Как-их-устанавливать)
+ [15. Что такое OptimisticLock? Расскажите стратегии создания через version, timestamp.](#15-Что-такое-OptimisticLock-Расскажите-стратегии-создания-через-version-timestamp)
+ [16. Расскажите про стратегии извлечения данных urgy, lazy?](#16-Расскажите-про-стратегии-извлечения-данных-eager-lazy)
+ [17. Что такое объект Proxy? С чем связана ошибка LazyInitializationException? Как ее избежать?](#17-Что-такое-объект-Proxy-С-чем-связана-ошибка-LazyInitializationException-Как-ее-избежать)
+ [18. HQL. Расскажи основные элементы синтаксиса HQL? Простой запрос, запрос join? Создания объекта через конструтор.](#18-HQL-Расскажи-основные-элементы-синтаксиса-HQL-Простой-запрос-Запрос-join-Создания-объекта-через-конструтор)
+ [19. Расскажите про уровни кешей в hibernate?](#19-Расскажите-про-уровни-кешей-в-hibernate)
+ [20. Что такое StatelessSessionFactory? Зачем он нужен, где он используется?](#20-Что-такое-StatelessSessionFactory-Зачем-он-нужен-где-он-используется)
+ [21. Зачем нужен режим read-only?](#21-Зачем-нужен-режим-read-only)
+ [22. Назовите некоторые важные аннотации, используемые для отображения в Hibernate](#22-Назовите-некоторые-важные-аннотации-используемые-для-отображения-в-Hibernate)
+ [23. Как реализованы Join’ы Hibernate?](#23-как-реализованы-joinы-hibernate)
+ [24. Почему мы не должны делать Entity class как final?](#24-Почему-мы-не-должны-делать-Entity-class-как-final)
+ [25. Что такое Named SQL Query?](#25-Что-такое-Named-SQL-Query)
+ [26. Каковы преимущества Named SQL Query?](#26-Каковы-преимущества-Named-SQL-Query)
+ [27. Расскажите о преимуществах использования Hibernate Criteria API.](#27-Расскажите-о-преимуществах-использования-Hibernate-Criteria-API)
+ [28. Как логировать созданные Hibernate SQL запросы в лог-файлы?](#28-Как-логировать-созданные-Hibernate-SQL-запросы-в-лог-файлы)
+ [29. Как управлять транзакциями с помощью Hibernate?](#29-Как-управлять-транзакциями-с-помощью-Hibernate)
+ [30. Что такое каскадные связи обновления и какие каскадные типы есть в Hibernate?](#30-Что-такое-каскадные-связи-обновления-и-какие-каскадные-типы-есть-в-Hibernate)
+ [31. Какие паттерны применяются в Hibernate?](#31-Какие-паттерны-применяются-в-Hibernate)
+ [32. Расскажите о Hibernate Validator Framework.](#32-Расскажите-о-Hibernate-Validator-Framework)
+ [33. Best Practices в Hibernate.](#33-Best-Practices-в-Hibernate)

> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate появился в далеком 2001 году. Тогда JPA еще не было. JPA появилось во многом стараниями HIBERNATE и только в 2006.
К этому времени у HIBERNATE был большой наработанный функционал, а в для первой версии JPA удалось согласовать
(с другими фирмами-разработчиками аналогичных продуктов) только часть этого объема функционала.
Поэтому, разработчики HIBERNATE сознательно (чтобы не было путанницы) пошли на то, чтобы в HIBERNATE имелось два пути работы:
> * старый путь - нативный HIBERNATE (через интерфейс Session)
> * и новый путь JPA (через интерфейс EntityManager).
>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Интерфейсы разные, методы как правило имеют одинаковые названия.
Различия есть и в других элементах - например, HIBERNATE конфигурируется файлом hibernate.cfg.xml, основанном на dtd,
а JPA - persistence.xml, основанном на xsd, кроме того, во многоих областях приняты различные правила по-умолчанию.
При этом, функциональность нативного HIBERNATE значительно больше, чем у JPA.
>
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
HIBERNATE не делал две различные реализации - одну для Session, а другую для EntityManager.
Реализация EntityManager является оберткой (wrap) реализации Session. Класс SessionImpl реализует интерфейс Session,
а Session расширяет интерфейс EntityManager. Если Вы выбираете путь JPA, то всегда имеете возможность быстро перейти,
на другие реализации JPA- EclipseLink, OpenJPA, DataNucleus (и узнаете насколько они совместимы).
Такая возможность может показаться единственным преимущество EntityManager перед Session, при существенно меньшем объеме функциональности.
При желании, можно работая с EntityManager дотянуться через unwrap до интерфейса Session и воспользоваться его функционалом
(ведь реализация у этих интерфейсов единая), но тогда при переходе на другие реализации такие места нужно будет переписывать особо.
Такая техника, рекомендуется разработчиками HIBERNATE.
>
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Но главное, что разработчики HIBERNATE подают его (HIBERNATE) во-первых как реализацию JPA и только во-вторых вспоминают
о дополнительных возможностях Session. В путевых картах 6 и 7 версии HIBERNATE, фактически говорится о том,
что JPA будет вбирать в себя самое лучшее, а остальное в HIBERNATE будет как бы deprecated. В последних версиях пятого HIBERNATE,
например, у Session стал deprecated createCriteria и эта часть перенесена в JPA.
----

### 1. Что такое ORM

Object-relational mapping
<br>ORM — это отображение POJO (со всеми полями, значениями, отношениями м/у друг другом) в структуры реляционных баз данных.

[к оглавлению](#Hibernate)

---

### 2. Опиши как конфигурируется Hibernate Рассказать про hibernate.cfg.xml и про mapping

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для корректной работы Hibernate, мы должны передать ему подробную информацию, которая связывает наши Java-классы c
таблицами в БД. Мы, также, должны укзать значения определённых свойств Hibernate.
* hibernate.dialect <br>- указывает Hibernate диалект БД для того, чтобы он генерировал необходимые SQL-запросы.
* hibernate.connection-driver_class <br> - класс JDBC драйвера.
* hibernate.connection.url <br>- адрес необходимой БД.
* hibernate.connection.username
* hibernate.connection.password <br>- пользователь и пароль для подключения к БД
* hibernate.connection.pool_size <br>- количество соединений, которые находятся в пуле соединений Hibernate.
* hibernate.connection.autocommit <br>- режим autocommit для JDBC-соединения.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Существует четыре способа конфигурации работы с Hibernate: 
* используя аннотации;
* hibernate.cfg.xml;
* hibernate.properties;
* persistence.xml.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Самый частый способ конфигурации : через аннотации и файл persistence.xml, что касается файлов hibernate.properties и hibernate.cfg.xml, то hibernate.cfg.xml главнее (если в приложение есть оба файла, то принимаются настройки из файла hibernate.cfg.xml). Конфигурация аннотациями, хоть и удобна, но не всегда возможна, к примеру, если для разных баз данных или для разных ситуаций вы хотите иметь разные конфигурацию сущностей, то следует использовать xml файлы конфигураций.
обычно, вся эта информация помещена в отдельный файл, либо XML-файл – hibernate.cfg.xml, либо – hibernate.properties.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В конфигурационном файле прописываются классы сущностей для маппинга, так же их можно указать программно,
импользуя класс Config и его метод addClass(uri), так же в Config можно передать конфигурационные файлы и
задать прочие конфигурационные параметры (как альтернатива xml)

[к оглавлению](#Hibernate)

---

### 3. Жизненный цикл Entity

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Кроме объектно-реляционного маппирования, одной из проблем, для решения которой был предназначен Hibernate,
является проблема управления объектами во время выполнения. Идея «persistence context (контекста постоянства)» - решение этой проблемы в Hibernate.
Persistence context можно рассматривать как контейнер или кеш первого уровня для всех объектов, которые вы загрузили или сохранили в базе данных во время сессии.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Сессия - это логическая транзакция, начало и конец которой определяются бизнес-логикой приложения.
Когда вы работаете с базой данных через persistence context, и все ваши экземпляры сущности присоединены к этому контексту,
у вас всегда должен быть один экземпляр сущности для каждой записи базы данных, с которой вы взаимодействовали во время сеанса.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Интерфейс сессии имеет несколько методов, которые в конечном итоге приводят к сохранению данных в базе данных:
persist, save, update, merge, saveOrUpdate. Чтобы понять разницу между этими методами, мы должны сначала обсудить цель сессии
и разницу между состояниями экземпляров сущностей во время существования в сессии.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Любой экземпляр сущности в вашем приложении существует в одном из трех основных состояний по отношению к session persistence context:
* transient - это просто объект сущности, который не имеет представления в постоянном хранилище и не управляется никаким сеансом.
У этого экземпляра нет соответствующих строк в базе данных. Обычно это просто новый объект, который вы создали для сохранения в базе данных.
* persistent  - этот экземпляр связан с уникальным объектом Session; после сброса (flushing) данных сессии в кэш первого уровня,
этот объект гарантированно будет иметь соответствующую запись в базе данных.
* detached - это экземпляр сущности, который когда-то был связан с какой-то сессией (в persistent state). Просто обычный объект POJO,
ID которого соответствует строке базы данных. Отличие от управляемого объекта в том, что он больше не привязан к какому-то persistence context.

[к оглавлению](#Hibernate)

---

### 4. Зачем нужен класс SessionFactory Является ли он потокобезопасным

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
SessionFactory используется для получения объектов session. SessionFactory отвечает за считывание параметров конфигурации Hibernate
и подключение к базе данных. Обычно в приложении имеется только один экземпляр SessionFactory и потоки, обслуживающие клиентские запросы,
получают экземпляры session с помощью объекта SessionFactory. Внутреннее состояние SessionFactory неизменно (immutable).
Internal state (внутреннее состояние) включает в себя все метаданные об Object/ Relational Mapping и задается при создании SessionFactory.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
SF также предоставляет методы для получения метаданных класса и статистики, вроде данных о втором уровне кэша, выполняемых запросах и т.д.
Т.к. объект SessionFactory immutable, то он потокобезопасный. Множество потоков может обращаться к одному объекту одновременно.

[к оглавлению](#Hibernate)

---

### 5. Зачем нужен класс Session Является ли он потокобезопасным

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Session — это основной интерфейс, который отвечает за связь с базой данных. Так же, он помогает создавать объекты запросов для
получения персистентных объектов. (персистентный объект — объект который уже находится в базе данных; объект запроса — объект который получается когда мы получаем результат
запроса в базу данных, именно с ним работает приложение). Обьект Session можно получить из SessionFactory:
```
Session session = sessionFactory.openSession();
```
Роль интерфейса Session:
* является оберткой для jdbc подключения к базе данных;
* является фабрикой для транзакций (согласно официальной документации transaction — аllows the application to define units of work, что , по сути, означает что транзакция
определяет границы операций связанных с базой данных).
* является хранителем обязательного кэша первого уровня.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Жизненный цикл объекта session связан с началом и окончанием транзакции. Этот объект предоставляет методы для 
CRUD (create, read, update, delete) операций для объекта персистентности. С помощью этого экземпляра можно выполнять HQL, SQL запросы и задавать критерии выборки.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Объект Hibernate Session не является потокобезопасным. Каждый поток должен иметь свой собственный объект Session и закрывать его по окончанию.

[к оглавлению](#Hibernate)

---

### 6. В чем отличие методов Session.get Session.load
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Оба метода служат для загрузки данных из БД, но load() служит для ленивой загрузки, в то время как
get() загружает данные непосредственно при вызове метода.
* load() бросает исключение, когда данные не найдены. Поэтому его нужно использовать только при уверенности в существовании данных.
* Нужно использовать метод get(), если необходимо удостовериться в наличии данных в БД.

[к оглавлению](#Hibernate)

---

### 7. Расскажите про методы flush close

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
;***flush()*** синхронизирует базу данных с текущим состоянием объекта/объектов,
хранящихся в памяти, но не коммитит транзакцию. Если было получено какое-либо исключение после вызова ***flush()***,
транзакция может быть отменена. Если синхронизировать базу данных небольшими порциями данных с помощью ***flush()***
и не коммитить транзакцию, можно переполнить кэш первого уровня и получить OOM.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
***commit()*** фиксирует транзакцию. Невозможно откатить транзакцию после успешного выполнения commit ().

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Режим FlushMode.AUTO:
* Во время выполнения flush, Hibernate транслирует изменения записанные текущим Persistence контекстом в SQL запросы.
Когда мы переводим JPA сущности из одного состояния в другое, то есть вызываем для них методы сохранения, удаления
(persist(), merge(), remove()), немедленного выполнения SQL-команд не происходит. SQL-команды выполняются лениво, при определенных условиях:
* подтверждения транзакции (commit()),
* выполнения JPQL и HQL запросов,
* выполнения  native SQL запросов.
* метода flush() – с помощью него мы можем явно выполнить накопившиеся SQL-команды.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Автоматический режим сброса изменений в базу FlushMode.AUTO установлен по-умолчанию; а изменения сбрасываются в базу
в определенном порядке: persist() выполняется перед remove().
Вообще при сбросе изменений в базу операции выполняются в таком порядке:
<br>OrphanRemovalAction
<br>AbstractEntityInsertAction
<br>EntityUpdateAction
<br>QueuedOperationCollectionAction
<br>CollectionRemoveAction
<br>CollectionUpdateAction
<br>CollectionRecreateAction
<br>EntityDeleteAction

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Connection close() - Заканчивает сессию, осовождает JDBC-соединение и выполняет очистку.

[к оглавлению](#Hibernate)

---

### 8. В чем отличие методы save от saveOrUpdate и merge

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В hibernate есть 2 похожих метода: 

##### save() и persist().

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
У save() цель в основном такая же, как и persist, но у него другие детали реализации,
И он не относится к реализации jpa. В документации к этому методу строго указано, что он сохраняет экземпляр, «сначала присваивая
сгенерированный идентификатор». Метод гарантированно вернет сериализуемое значение этого идентификатора. persist имеет тип void,
save() возвращает Serializable - Id

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Т.о они оба предназначены для добавления нового экземпляра сущности в persistent context, то есть перехода экземпляра из transient
в persistent state. Т.к. persist относится к jpa, То он дает связанные с этим гарантии:
* transient entity становится persistent (и это верно для всех всвязанных сущностей, опреции происходят каскадно
cascade = PERSIST или cascade = ALL),
* если экземпляр уже является persistent, то этот вызов не имеет никакого эффекта для этого конкретного экземпляра,
(но он по-прежнему каскадно передается своим отношениям с помощью cascade = PERSIST или cascade = ALL),
* если экземпляр detached, следует ожидать исключения либо при вызове этого метода, либо при commit или flush сессии.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Важно** что в спецификации jpa не указано, что идентификатор будет сгенерирован сразу, независимо от стратегии генерации 
идентификатора. Спецификация для метода persist позволяет реализации выдавать операторы для генерации идентификатора при commit или flush сессии,
и не гарантируется, что идентификатор будет ненулевым после вызова persist, поэтому вам не следует полагаться на него.

##### Merge/Update

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Как и в случае с save() и persist(),
    метод Update - это «оригинальный» метод Hibernate, который присутствовал задолго до того,
как был добавлен метод Merge. Его семантика различается по нескольким ключевым моментам:
* он действует на переданный объект (имеет тип void), переводит переданный объект из detached  в persistent state
* бросает исключение, если вы передать ему transient объект.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Основное назначение метода слияния Merge - обновить постоянный экземпляр сущности новыми значениями полей из отдельного экземпляра сущности.
Метод Merge делает следующее:
* находит экземпляр сущности по ID, взятому из переданного объекта
(либо извлекается существующий экземпляр сущности из контекста постоянства, либо новый экземпляр загружается из базы данных);
* копирует поля из переданного объекта в этот экземпляр;
* возвращает недавно обновленный экземпляр.
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**!Важно** что метод merge возвращает объект - это объект mergedObj, который был загружен в persistence context и обновлен,
а не объект, переданный в качестве аргумента. Это два разных объекта, и от объект object уже не привязан к контексту.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Merge реализует jpa, т.о.:
* если объект detached, он копируется в существующий постоянный объект;
* если объект transient, он копируется на вновь созданный persistent объект;
* эта операция происходит каскадно для всех отношений с отображением cascade = MERGE или cascade = ALL;
* если объект является persistent, то вызов метода не влияет на него (но каскадные опреации происходят).

##### SaveOrUpdate()

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Метод присутствует только в Hibernate API и не имеет jpa аналога. Подобно update(), его также можно использовать для повторного подключения экземпляров.
Фактически, внутренний класс DefaultUpdateEventListener, который обрабатывает метод обновления, является подклассом DefaultSaveOrUpdateListener,
просто переопределяя некоторые функции. Основное отличие метода saveOrUpdate заключается в том, что он не генерирует исключение при применении
к transient экземпляру; вместо этого он делает этот временный экземпляр постоянным.
Можно думать об этом методе как об универсальном инструменте для создания persistent объекта независимо от его состояния, является ли он transient или detached.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Если у вас нет особых требований, как правило, вам следует придерживаться методов persist и merge, поскольку они стандартизированы и гарантированно соответствуют спецификации JPA.
Они также переносимы на случай, если вы решите переключиться на другого persistence provider,
но иногда они могут показаться не такими полезными, как «оригинальные» методы Hibernate, save, update и saveOrUpdate.

[к оглавлению](#Hibernate)

---

### 9. Расскажите процесс создания, редактирования, чтения и удаления данных через Hibernate
    
[к оглавлению](#Hibernate)

---

### 10. Как осуществляется иерархия наследования в Hibernate Рассказать про три стратегии наследования

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Главное противоречие между объектно-ориентированной и реляционной моделями заключается в том, объектная модель поддерживает два вида отношений
(«is a» — “является”, и «has a» — “имеет”), а модели, основанные на SQL, поддерживают только отношения «has a».

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Иными словами, SQL не понимает наследование типов и не поддерживает его. Поэтому на этапе построения сущностей и схемы БД одной из главных задач разработчика
будет выбор оптимальной стратегии представления иерархии наследования.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Всего таких стратегий 4:
* Использовать одну таблицу для каждого класса и полиморфное поведение по умолчанию.
* Одна таблица для каждого конкретного класса, с полным исключением полиморфизма и отношений наследования из схемы SQL (для полиморфного поведения во время выполнения будут использоваться UNION-запросы)
* Единая таблица для всей иерархии классов. Возможна только за счет денормализации схемы SQL. Определять суперкласс и подклассы будет возможно посредством различия строк.
* Одна таблица для каждого подкласса, где отношение “is a” представлено в виде «has a», т.е. – связь по внешнему ключу с использованием JOIN.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Можно выделить 3 главных фактора, на которые повлияет выбранная вами стратегия:
1) Производительность (мы используем “hibernate_show_sql”, чтобы увидеть и оценить все выполняемые к БД запросы)
2) Нормализация схемы и гарантия целостности данных (не каждая стратегия гарантирует выполнение ограничения NOT NULL)
3) Возможность эволюции вашей схемы

##### Стратегия 1 Одна таблица для каждого класса

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Полиморфизм в данном случае будет неявным. Каждый класс-потомок мы можем отразить с помощью аннотации **@Entity**

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**ВАЖНО!** Свойства суперкласса по умолчанию будут проигнорированы. Чтобы сохранить их в таблицу конкретного подкласса,
необходимо использовать аннотацию **@MappedSuperClass**. Отображение подклассов не содержит ничего необычного.
Единственное, на что следует обратить внимание – возможно, незнакомая для некоторых аннотация **@AttributeOverride.**
Она используется для переименования столбца в таблице подкласса, в том случае если названия у предка и таблицы потомка не совпадают.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Главная проблема при использовании данной стратегии заключается в том, что использовать полиморфные ассоциации
в полной мере будет невозможно: обычно они представлены в БД в виде доступа по внешнему ключу, а у нас попросту нет таблицы super_class.
А поскольку каждый объект super_class будет в приложении связан с конкретным объектом super_class_impl, то каждой из таблиц-«потомков» нужен будет внешний ключ, ссылающийся на таблицу super_class_impl.
для каждого конкретного подкласса Hibernate использует отдельный SELECT-запрос.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Другой важной проблемой при использовании данной стратегии будет сложность рефакторинга. Изменение 
названия полей в суперклассе вызовет необходимость изменения названий во многих таблицах и потребует ручного переименования 
(инструменты большинства IDE не учитывают **@AttributeOverride**). В случае, если в вашей схеме не 2 таблицы, а 50, это чревато большими 
временными затратами.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Этот подход возможно использовать только для верхушки иерархии классов, где:
- Полиморфизм не нужен (выборку для конкретного подкласса Hibernate будет выполнять в один запрос -> производительность будет высокой)
- Изменения в суперклассе не предвидятся.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для приложения, где запросы будут ссылаться на родительский класс SuperClass эта стратегия не подойдет.
Напимер можно создать
```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
```
И наследовать от него остальные сущности. Это все равно, что мы бы в каждой сущности прописали поля id, name

```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_users")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}
```

```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_books")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
```

##### Стратегия 2 Одна таблица для каждого класса с объединениями (UNION)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ограничение этого подхода заключается в том, что если свойство отображается в суперклассе,
имя столбца должно быть одинаковым для всех таблиц подклассов. Данная стратегия не поддерживает **@AttributeOverride.**

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Новой также будет указанная над суперклассом аннотация **@Inheritance** с указанием выбранной стратегии TABLE_PER_CLASS.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**ВАЖНО!** В рамках данной стратегии наличие идентификатора в суперклассе является обязательным требованием.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**ВАЖНО!** Согласно стандарту JPA стратегия TABLE_PER_CLASS не является обязательной, поэтому другими реализациями может не поддерживаться.
```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}

```
```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_users")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}

```
```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_books")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
```
Наша схема SQL по-прежнему ничего не знает о наследовании; между таблицами нет никаких отношений.

Главное преимущество данной стратегии можно увидеть, выполнив полиморфный запрос
```
session.createQuery("from " + BaseEntity.class.getSimpleName(), BaseEntity.class).list().forEach(System.out::println);
```
для первой стратегии hibernate брасает исключение:
```
 java.lang.IllegalArgumentException: org.hibernate.hql.internal.ast.QuerySyntaxException: BaseEntity is not mapped [from BaseEntity]
```
Т.е. любой класс, объявленный как суперкласс с аннотацией **@MappedSuperclass** не может быть сущностью и не может быть запрошен.
Он предоставляет предопределенную логику для всех своих подклассов. 

При применении второй стратегии hibernate генерирует следующие запросы:
```
Hibernate: 
    /* 
from
    BaseEntity */ select
        baseentity0_.id as id1_0_,
        baseentity0_.name as name2_0_,
        baseentity0_.user_address as user_add1_2_,
        baseentity0_.user_phone as user_pho2_2_,
        baseentity0_.book_author as book_aut1_1_,
        baseentity0_.book_pages as book_pag2_1_,
        baseentity0_.clazz_ as clazz_ 
    from
        ( select
            id,
            name,
            user_address,
            user_phone,
            null::varchar as book_author,
            null::int8 as book_pages,
            1 as clazz_ 
        from
            h_users 
        union
        all select
            id,
            name,
            null::varchar as user_address,
            null::varchar as user_phone,
            book_author,
            book_pages,
            2 as clazz_ 
        from
            h_books 
    ) baseentity0_
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;В данном случае Hibernate использует FROM, чтобы извлечь все экземпляры BaseEntity из всех
таблиц подклассов. Таблицы объединяются с помощью UNION, а в промежуточный результат добавляются литералы (1 и 2).
Литералы используются Hibernate для создания экземпляра правильного класса.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Объединение таблиц требует одинаковой структуры столбцов, поэтому вместо 
несуществующих столбцов были вставлены NULL (например, «null::varchar as user_address» в h_books – в таблице книг нет названия адреса).

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Другим важный преимуществом по сравнению с первой стратегией будет возможность
использовать полиморфные ассоциации. Теперь можно будет без проблем отобразить ассоциации между классами
User и BaseEntity.

##### Стратегия 3 Единая таблица для всей иерархии классов

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Иерархию классов можно целиком отобрать в одну таблицу. Она будет содержать столбцы для всех полей каждого класса иерархии. Для каждой записи конкретный подкласс будет определяться значением дополнительного столбца с селектором.

Наша схема теперь выглядит вот так:

BaseEntity:
* id: int
* bd_type: varchar(2)
* name: varchar(20)
* author: varchar(20)
* pages: bigint
* address: varchar(50)
* phone: varchar(12)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Для создания отображения с одной таблицей необходимо использовать стратегию наследования SINGLE_TABLE.
Корневой класс будет отображен в таблицу base_entity. Для различения типов будет использован столбец селектора.
Он не является полем сущности и создан только для нужд Hibernate.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**ВАЖНО!** Если не указать столбец селектора в суперклассе явно – он получит название по умолчанию DTYPE и тип VARCHAR.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Каждый класс иерархии может указать свое значение селектора с помощью аннотации **@DiscriminatorValue.**
Не стоит пренебрегать явным указанием имени селектора: по умолчанию Hibernate будет использовать полное имя класса или имя сущности (зависит от того, используются ли файлы XML-Hibernate или xml-файлы JPA/аннотации).

Создаем классы:
```java
@Entity
@Data
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "BD_TYPE")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
```
```java
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("HU")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}
```
```java
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("HA")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
```
Выполняем тот-же запрос:
```
session.createQuery("from " + BaseEntity.class.getSimpleName(), BaseEntity.class).list().forEach(System.out::println);
```
результат:
```
Hibernate: 
    /* 
from
    BaseEntity */ select
        baseentity0_.id as id2_0_,
        baseentity0_.name as name3_0_,
        baseentity0_.user_address as user_add4_0_,
        baseentity0_.user_phone as user_pho5_0_,
        baseentity0_.book_author as book_aut6_0_,
        baseentity0_.book_pages as book_pag7_0_,
        baseentity0_.BD_TYPE as bd_type1_0_ 
    from
        base_entity baseentity0_
```
Если же запрос выполняется к конкретному подклассу – будет просто добавлена строка «where BD_TYPE = “HU”».
<br>Вот как будет выглядеть отображение в единую таблицу:
```
 bd_type | id | name | user_address | user_phone | book_author | book_pages
---------+----+------+--------------+------------+-------------+------------
 HU      |  1 | name | address      | phone      |             |
 HA      |  2 | name |              |            | author      |        300
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;В случае, когда схема была унаследована, и добавить в нее столбец селектора
невозможно, на помощь приходит аннотация **@DiscriminatorFormula**, которую необходимо добавить к родительскому классу.
В нее необходимо передать выражение CASE...WHEN.
```java
@Entity
@Data
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("CASE WHEN CARD_NUMBER IS NOT NULL THEN 'HU' ELSE 'HB' END")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Главным плюсом данной стратегии является производительность. Запросы (как полиморфные, так и неполиморфные) выполняются
очень быстро и могут быть легко написаны вручную. Не приходится использовать соединения и объединения. Эволюция схемы 
также производится очень просто.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Однако, проблемы, сопровождающие эту стратегию, часто будут перевешивать ее преимущества.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Главной из них является целостность данных. Столбцы тех свойств, которые объявлены в подклассах, могут содержать NULL.
В результате простая программная ошибка может привести к тому, что в базе данных окажется книга без автора или без страниц.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Другой проблемой будет нарушение нормализации, а конкретно – третьей нормальной формы. В этом свете выгоды от повышенной производительности уже выглядят сомнительно. Ведь придется, как минимум, пожертвовать удобством сопровождения: в долгосрочной перспективе денормализованные схемы не сулят ничего хорошего

##### Стратегия 4 Одна таблица для каждого класса с использованием соединений (JOIN)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Схема наших классов в сравнении с 3й стратегией останется неизменной:


User -> | BaseEntity | <-Book
------------ | ------------- | -------------
 address: String | id: int  | author: String
 phone: String |name: String | pages: long

А вот в схеме БД произошли некоторые изменения

h_user —id:id—> | base_entity | <—id:id— h_book
------------ | ------------- | -------------
id: int |  id: int | id: int
address: varchar(50) |name: varchar(20)   | author: varchar(20)
phone: varchar(12) | | pages: bigint
 
```
create table base_entity
(
	id integer not null
		constraint base_entity_pkey
			primary key,
	name varchar(20) not null
);

create table h_user
(
	id integer not null
		constraint h_user_pkey
			primary key
		constraint h_user_base_entity_id_fk
			references base_entity,
	address varchar(50) not null,
	phone varchar(12) not null
);

create table h_book
(
	id integer not null
		constraint h_book_pkey
			primary key
		constraint h_book_base_entity_id_fk
			references base_entity,
	author varchar(20) not null,
	pages bigint not null
);

```
В Java-коде для создания такого отображения необходимо использовать стратегию JOINED:
```java
@Entity
@Data
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
```
```java
@Entity
@Data
@NoArgsConstructor
@Table(name = "h_user")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}
```
```java
@Entity
@Data
@NoArgsConstructor
@Table(name = "h_book")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Теперь при сохранении, например, экземпляра User, Hibernate вставит две записи. В таблицу base_entity попадут свойства,
объявленные в полях суперкласса BaseEntity, а значения полей подкласса User будут записаны в таблицу h_user.
Эти записи будут объединены общим первичным ключом.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Таким образом, схема была приведена в нормальное состояние. Эволюция схемы и определение ограничений целостности также осуществляются просто.
Внешние ключи позволяют представить полиморфную ассоциацию с конкретным подклассом.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В результате в бд будут следующие таблицы:
```
 Схема  |                Имя                 |        Тип         | Владелец
--------+------------------------------------+--------------------+----------
 public | base_entity                        | таблица            | postgres
 public | h_book                             | таблица            | postgres
 public | h_books                            | таблица            | postgres
 public | h_user                             | таблица            | postgres
 public | h_users                            | таблица            | postgres

base_entity         h_book                          h_user          
 id | name       book_author | book_pages | id       user_address | user_phone | id
----+------     -------------+------------+----     --------------+------------+---- 
  1 | name       author      |        300 |  2       address      | phone      |  1 
  2 | name
```
Выполняем тот-же запрос:
```
session.createQuery("from " + BaseEntity.class.getSimpleName(), BaseEntity.class).list().forEach(System.out::println);
```
результат:
```
Hibernate: 
    /* 
from
    BaseEntity */ select
        baseentity0_.id as id1_0_,
        baseentity0_.name as name2_0_,
        baseentity0_1_.user_address as user_add1_2_,
        baseentity0_1_.user_phone as user_pho2_2_,
        baseentity0_2_.book_author as book_aut1_1_,
        baseentity0_2_.book_pages as book_pag2_1_,
        case 
            when baseentity0_1_.id is not null then 1 
            when baseentity0_2_.id is not null then 2 
            when baseentity0_.id is not null then 0 
        end as clazz_ 
    from
        base_entity baseentity0_ 
    left outer join
        h_user baseentity0_1_ 
            on baseentity0_.id=baseentity0_1_.id 
    left outer join
        h_book baseentity0_2_ 
            on baseentity0_.id=baseentity0_2_.id
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Предложение CASE…WHEN позволяет Hibernate определить конкретный подкласс для каждой записи. В нем проверяется наличие
либо отсутствие строк в таблицах подклассов User и Book с помощью литералов.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Подобную стратегию будет весьма непросто реализовать вручную. Даже реализовать отчеты на основе произвольных запросов
будет значительно сложнее.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Производительность также может оказаться неприемлемой для конкретного проекта, поскольку запросы потребуют соединения нескольких таблиц или многих последовательных операций чтения.

##### Смешение стратегий отображения наследования
      
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
При работе со стратегиями TABLE_PER_CLASS, SINGLE_TABLE и JOINED значительным неудобством является тот факт, что между ними невозможно переключаться. Выбранной стратегии придется придерживаться до конца (либо полностью менять схему).

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Но есть приемы, с помощью которых можно переключить стратегию отображения для конкретного подкласса.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Например, отобразив иерархию классов в единственную таблицу (стратегия 3), можно выбрать для отдельного подкласса стратегию с отдельной таблицей и внешним ключом (стратегия 4).

[Статья на ХАБР об этом](https://habr.com/ru/post/337488/)

[к оглавлению](#Hibernate)

---

### 11. Можно ли создать собственный тип данных

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Из коробки Hibernate поддерживает некий общий набор типов данных SQL и типов данных Java, а также отображений между ними.
В основном в этот набор входят базовые вещи, такие как даты, строки, числа, блобы и так далее.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Набор этих типов довольно общий и поддерживается большинством баз данных, с которыми работает Hibernate.
С другой стороны, каждая база может иметь свои собственные, уникальные и, следовательно, неподдерживаемые типы данных.
Со стороны Java, в свою очередь, тоже можно представить собственные структуры данных, которые напрямую не
поддерживаются в Hibernate, но желательно иметь возможность их сохранять.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для решения этой проблемы в Hibernate существует поддержка пользовательских типов данных. То есть можно написать,
как тот или иной класс в Java должен сохраняться в тот или иной тип данных (читай столбец в таблице) в SQL базе.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для этого нужно сначала описать пользовательский тип (создать Java класс, с нужнми полями, геттерами/сеттерами).
Класс должен быть implements Serializable. 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Чтобы реализовать поддержку собственного типа данных в Hibernate, необходимо реализовать интерфейс UserType и его методы.
Тут, по сути, содержится вся логика сериализации/десериализации в БД.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
И там, где поле сущности имеет пользовательский тип, нужно указать это **@Type**(type = "класс имплементирующий UserType")

[Статья об этом](https://easyjava.ru/data/hibernate/polzovatelskie-tipy-v-hibernate/)

[к оглавлению](#Hibernate)

---

### 12. Какие коллекции поддерживаются на уровне mapping

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate позволяет сохранять (persist) коллекции. Эти постоянные коллекции могут содержать почти любой другой тип
Hibernate, включая: базовые типы, пользовательские типы, компоненты и ссылки на другие сущности. Различие между
значением и ссылочной семантикой в этом контексте очень важно. Объект в коллекции может обрабатываться семантикой 
«значение» (ее жизненный цикл полностью зависит от владельца коллекции), или это может быть ссылка на другую сущность
с её собственным жизненным циклом. В последнем случае считается, что только «ссылка (link)» между двумя объектами
является состоянием, удержанным коллекцией.
* Bag
* Set
* List
* Array
* Map

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Своей реализации тип коллекции Bag очень напоминает Set, разница состоит в том, что Bag может хранить повторяющиеся значения.
Bag хранит непроиндексированный список элементов. Большинство таблиц в базе данных имеют индексы отображающие положение
элемента данных один относительно другого, данные индексы имеют представление в таблице в виде отдельной колонки.
При объектно-реляционном маппинге, значения колонки индексов мапится на индекс в Array, на индекс в List или на key 
в Map. Если вам надо получить коллекцию объектов не содержащих данные индексы, то вы можете воспользоваться 
коллекциями типа Bag или Set (коллекции содержат данные в неотсортированном виде, но могут быть отсортированы
согласно запросу).

[к оглавлению](#Hibernate)

---

### 13. Зачем нужен класс Transactional

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Transaction (org.hibernate.Transaction) — однопоточный короткоживущий объект, используемый для атомарных операций.
Это абстракция приложения от основных JDBC или JTA транзакций. org.hibernate.Session может занимать несколько
org.hibernate.Transaction в определенных случаях.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Вместо вызовов session.openTransaction() и session.commit() используется аннотация @Transactional]


[Hibernate. Основные принципы работы с сессиями и транзакциями](https://habr.com/ru/post/271115/)

[к оглавлению](#Hibernate)

---

### 14. Расскажите про уровни изоляции Какие уровни поддерживаются в hibernate Как их устанавливать

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Модель строгой согласованности реляционной базы данных основана на свойствах транзакции ACID.
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
ACID описывает
требования к транзакционной системе 
* Atomicity — Атомарность (никакая транзакция не будет зафиксирована в системе частично)
* Consistency — Согласованность (каждая успешная транзакция по определению фиксирует только допустимые результаты)
* Isolation — Изолированность (во время выполнения транзакции параллельные транзакции не должны оказывать влияния на её результат)
* Durability — Долговечность (Независимо от проблем на нижних уровнях (к примеру, обесточивание системы или сбои в оборудовании) изменения, сделанные успешно завершённой транзакцией, должны остаться сохранёнными после возвращения системы в работу.)

##### Изоляция и последовательность

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В системе реляционных баз данных атомарность и долговечность являются строгими свойствами, а согласованность
и изоляция более или менее настраиваются. Мы не можем даже отделить последовательность от изоляции, поскольку
эти два свойства всегда связаны между собой.
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Чем ниже уровень изоляции, тем менее последовательной будет система. От наименьшего к наиболее последовательному существует четыре уровня изоляции:
* READ UNCOMMITTED - когда во время одновременного выполнения двух транзакций, транзакция А изменяет какие-то данные
(но не еще не коммитит), а танзакция В читает эти данные. Потом в танзакции А происходит ошибка и rollback, но танзакция
В по-прежнему работает с этими данными. В таком случае все будет работать очень быстро, но будут проблемы с консистентностью данных.
 
* READ COMMITTED (защита от грязного чтения) - чтение только закоммиченых данных

* REPEATABLE READ (защита от грязных и неповторяющихся чтений) - этот уровень изоляции обеспечивает повторяемость чтения одних данных
в рамках одной транзакции, даже если данные между чтениями были изменены. Сначала «B» выполняет запрос SELECT и блокирует выбранные
строки. После этого «А» выполняет запрос INSERT. «B» выполняет новый запрос SELECT с теми же условиями, что и первый.
«B» теперь будет видеть те же результаты, что и раньше (второй SELECT должен быть выполнен в той же транзакции, что и
первый).

* SERIALIZABLE (защита от грязных, неповторяющихся чтений и фантомных чтений) - этот уровень является наиболее безопасным.
Но с другой стороны, это и самое медленное решение. Когда транзакция «B» считывает данные, она блокирует всю таблицу 
данных. Это означает, что другая транзакция не может изменить данные в этой таблице.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Хотя наиболее последовательный уровень изоляции SERIALIZABLE был бы наиболее безопасным, большинство баз данных
по умолчанию вместо этого используют READ COMMITTED. Т.к. для обеспечения возможности одновременного выполнения
большего количества транзакций нам необходимо сократить долю последовательной обработки данных. Чем меньше время
удержания блокировки, тем больше throughput.

##### Уровни изоляции

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Как правило, база данных используется совместно несколькими приложениями, и у каждого из них есть свои специфические
требования к транзакциям. Для большинства транзакций уровень изоляции READ COMMITTED - лучший выбор, и мы должны
переопределять его только для определенных бизнес-случаев.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Эта стратегия оказывается очень эффективной, позволяя нам иметь более строгие уровни изоляции для всего множества
возможных SQL транзакций.

##### Уровень изоляции источника данных

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
JDBC Connection позволяет нам установить уровень изоляции для всех транзакций, выполняемых в этом конкретном соединении.
Установление нового соединения с базой данных - процесс, требующий больших затрат ресурсов, поэтому большинство приложений используют
пул соединений. Пул соединений также может установить свой уровень изоляции транзакции.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
По сравнению с настройкой уровня изоляции глобальной базы данных, конфигурации изоляции транзакции уровня pool DataSource более удобны.
Каждое приложение может устанавливать свой собственный уровень управления параллелизмом.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Мы даже можем определить несколько источников данных, каждый с заранее определенным уровнем изоляции. Таким образом,
мы можем динамически выбирать конкретный уровень изоляции JDBC Connection.

##### Уровень изоляции Hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate должен поддерживать как JDBC транзакции, так и транзакции JTA, соответственно он предлагает очень гибкий механизм поставщика соединений.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Чтобы настроить уровень изоляции транзакций в Hibernate, нам нужно изменить свойство, называемое hibernate.connection.isolation.
Это свойство может принимать одно из следующих значений:

1. (read uncommitted)
2. (read committed)
4. (repeatable read)
8. (serializable)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Обычно уровень изоляции устанавливается на уровне java.sql.Connection с помощью метода setTransactionIsolation (int level).
Уровень, переданный в параметре, должен быть одним из констант Connection:

* Connection.TRANSACTION_READ_UNCOMMITTED,
* Connection.TRANSACTION_READ_COMMITTED,
* Connection.TRANSACTION_REPEATABLE_READ
* Connection.TRANSACTION_SERIALIZABLE. 

##### Spring @Transactional

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Аннотация Spring ***@Transactional*** используется для определения границы транзакции. В отличие от Java EE, эта аннотация позволяет
нам настраивать:
* уровень изоляции  @Transactional(isolation = Isolation.SERIALIZABLE)

* политика rollback и типов исключений

* propagation - @Transactional(propagation=Propagation.REQUIRED) все варианты : REQUIRES_NEW, MANDATORY, SUPPORTS, NOT_SUPPORTED, NEVER и NESTED.

* read-only

* timeout - @Transactional(timeout=60) По умолчанию используется таймаут, установленный по умолчанию для базовой транзакционной системы.
Сообщает менеджеру tx о продолжительности времени, чтобы дождаться простоя tx, прежде чем принять решение об откате не отвечающих транзакций.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Поскольку большинство реализаций DataSource могут иметь только уровень изоляции транзакции по умолчанию, у нас может быть
несколько таких источников данных, каждый из которых обслуживает соединения для определенного уровня изоляции транзакции.

[Статья 1](https://habr.com/ru/post/269485/#2_2_2)
<br>
[Статья 2](https://vladmihalcea.com/a-beginners-guide-to-transaction-isolation-levels-in-enterprise-java/)


[к оглавлению](#Hibernate)

---

### 15. Что такое OptimisticLock Расскажите стратегии создания через version timestamp

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Блокировки служат для упраления параллельным доступом к базе данных. Мы должны иметь возможность обрабатывать несколько транзакций эффективно
и, что самое главное, верно (acid). Стратегия блокировок может быть либо оптимистичной, либо пессимистичной.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**Оптимистичные** блокировки ***предполагают***, что множество транзакций могут завершиться ***без влияния друг на друга***, и таким образом
могут выполнятся без блокировок тех ресурсов, на которые они влияют. Перед коммитом, каждая транзакция проверяет, что ни одна другая
транзакция не модифицировала ее данные. Если проверка выявила конфликтующие модификации, транзакция, находящаяся в состоянии коммита,
откатывается.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**Пессимистичная** стратегия ***предполагает***, что параллельные транзакции ***будут конфликтовать друг с другом***, и требует блокировки
ресурсов после их чтения, а также ее снятия только после того, как приложение завершило использование данных.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate предоставляет механизмы для реализации обеих стратегий блокировок в приложении.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Чтобы использовать оптимистическую блокировку, нам нужен объект, включающий свойство с аннотацией ***@Version***.
При его использовании каждая транзакция, считывающая данные, содержит значение свойства версии. Прежде чем транзакция выполнит обновление,
она снова проверяет свойство версии. Если за это время значение изменилось, генерируется исключение ***OptimisticLockException***.
В противном случае транзакция фиксирует обновление и увеличивает значение свойства версии.
```
@Entity
@Data
public class Flight implements Serializable {
...
@Version
@Column(name="OPTLOCK")
public Integer getVersion() { ... }
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Временные метки (timestamps) — менее надежный способ оптимистичных блокировок чем номера версий, который также может быть использован
приложениями для других целей. Временные метки используются автоматически, если вы используете аннотацию ***@Version*** на свойстве типа Date
или Calendar.
```
@Entity
public class Flight implements Serializable {
...
@Version
public Date getLastUpdate() { ... }
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Оптимистическая блокировка основана на обнаружении изменений в сущностях путем проверки их атрибута версии. Если происходит какое-либо
одновременное обновление, возникает исключение ***OptimisticLockException***. После этого можно повторить попытку обновления данных.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Этот механизм подходит для приложений, которые выполняют гораздо больше операций чтения, чем обновления или удаления. Более того, это
полезно в ситуациях, когда сущности необходимо отсоединить (detached) на некоторое время, а блокировки удерживать нельзя.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Механизм пессимистической блокировки напротив блокирует сущности на уровне базы данных.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Каждая транзакция может захватить блокировку. Пока блокировка удерживается, любая другая транзакция не может читать, удалять или обновлять
заблокированные данные. Т.о. использование пессимистической блокировки потенциально может привести к deadlock. Однако это обеспечивает
большую целостность данных, чем оптимистическая блокировка.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Класс LockMode определяет различные уровни блокировок, которые может захватывать Hibernate.

* LockMode.WRITE
    - Захватывается автоматически, когда Hibernate обновляет или вставляет строку.
* LockMode.UPGRADE
    - Захватывается после явного запроса пользователя с использованием SELECT… FOR UPDATE на БД, поддерживающих данный синтаксис.
* LockMode.UPGRADE_NOWAIT
    - Захватывается после явного запроса пользователя с использованием SELECT… FOR UPDATE NOWAIT в Oracle
* LockMode.READ
    - Захватывается автоматически когда Hibernate читает данные под уровнями изоляции Repeatable Read или Serializable. Может быть повторно захвачен явным запросом пользователя.
* LockMode.NONE
    - Отсутствие блокировки. Все объекты переключаются на этот режим блокировки в конце транзакции. Объекты, ассоциированные с сессией
через вызов update() или saveOrUpdate также находятся в этом режиме .

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Явный запрос пользователя, обозначенный выше, происходит как следствие любых из следующих действий:
* Вызов Session.load(), с указанием LockMode
* Вызов Session.lock()
* Вызов Query.setLockMode()

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Если вы вызовете ***Session.load()*** с опцией **UPGRADE** или **UPGRADE_NOWAIT**, и запрошенный объект еще не подгрузился сессией, объект
подгружается с помощью **SELECT… FOR UPDATE**. Если вы вызовете ***load()*** для объекта, которые уже подгружен с менее строгой блокировкой,
чем с той, что вы запросили, Hibernate вызовет ***lock()*** для этого объекта.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
***Session.lock()*** осуществляет проверку номера версии в режимах **READ**, **UPGRADE**, или **UPGRADE_NOWAIT**. В случаях **UPGRADE** или
**UPGRADE_NOWAIT**, будет использован синтаксис **SELECT… FOR UPDATE**. Если запрошенный режим блокировки не поддерживается базой данных,
Hibernate будет использовать подходящий альтернативный режим вместо выбрасывания исключения. Это гарантирует переносимость приложений.

[к оглавлению](#Hibernate)

---

### 16. Расскажите про стратегии извлечения данных eager lazy

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Eager Loading - это design pattern, в котором загрузка связанных данных происходит сразу, каскадно. Загружаются все данные по цепочке.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Lazy Loading - это design pattern, который используется для отсрочки загрузки связанных данных на максимально возможное время.
Данные подгружаются при непосредственном обращении.

[к оглавлению](#Hibernate)

---

### 17. Что такое объект Proxy С чем связана ошибка LazyInitializationException Как ее избежать

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate использует прокси объект для поддержки Lazy Loading. Обычно при загрузке данных из таблицы Hibernate не загружает все
отображенные (замаппинные) объекты. Как только вы ссылаетесь на дочерний объект или ищите объект с помощью геттера, если связанная сущность
не находиться в кэше сессии, то прокси код перейдет к базе данных для загрузки связанной сущности. Для этого используется javassist 
(тулза для манипуляции с байткодом), чтобы эффективно и динамически создавать реализации подклассов ваших entity объектов.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate поддерживает ленивую инициализацию используя proxy объекты и выполняет запросы к базе данных только по необходимости.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
fetch = FetchType.LAZY это значит, что хибернейт не будет инициализировать эти поля пока вы к ним не обратитесь. Но если обратиться
к этим полям за пределами одной транзакции, он выкидывает ошибку. Чтобы этого избежать надо, чтобы метод,
который обращается к этим полям был с аннотацей ***@Transactional*** или загрузка всех необходимых данных была в границах одной транзакции

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Можно сжоинить необходимые данные или так: ***Hibernate.initialize(owner.getBooks());*** Этот хак заставит хибернейт инициировать коллекцию.
НО! Возможно это не всегда надо и тогда надо выбрать первый вариант и отталкиваться от здравого смысла, смотреть, где надо навешивать аннотацию, а где нет.

[к оглавлению](#Hibernate)

---

### 18. HQL Расскажи основные элементы синтаксиса HQL Простой запрос Запрос join Создания объекта через конструтор

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate Framework поставляется с мощным объектно-ориентированным языком запросов - Hibernate Query Language (HQL).
Он похож по внешнему виду на SQL. Однако, по сравнению с SQL, HQL полностью объектно-ориентирован и понимает такие понятия, как наследование, полиморфизм и ассоциация.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
За исключением имен классов и свойств Java, запросы не учитывают регистр. Таким образом, SeLeCT совпадает с SELEct таким же, как SELECT, но org.hibernate.eg.FOO не
является org.hibernate.eg.Foo, а foo.barSet не является foo.BARSET.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Запросы HQL кэшируются (это как плюс так и минус).

Например, если у нас есть эти два запроса
```
FROM Employee emp
JOIN emp.department dep
```
и
```
FROM Employee emp
JOIN FETCH emp.department dep
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В этих двух запросах вы используете JOIN для запроса всех сотрудников, с которыми связан хотя бы один отдел.
Но разница в том, что в первом запросе вы возвращаете только сотрудников для Hibernate. Во втором запросе вы
возвращаете сотрудников и все связанные с ними отделы. Итак, если вы используете второй запрос, вам не
нужно будет выполнять новый запрос, чтобы снова попасть в базу данных, чтобы увидеть отделы каждого сотрудника.
Вы можете использовать второй запрос, если уверены, что вам понадобится отдел каждого сотрудника. Если вам не нужен
отдел, воспользуйтесь первым запросом.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В HQL поддерживается только форма INSERT INTO ... SELECT ...; а не INSERT INTO ... VALUES ... .
Иструкции INSERT по своей сути не являются полиморфными. 

Пример кода
``` 
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

String hqlInsert = "insert into Account (id, name) select c.id, c.name from Customer c where ...";
int createdEntities = s.createQuery( hqlInsert ).executeUpdate();
tx.commit();
session.close();
```

[к оглавлению](#Hibernate)

---

### 19. Расскажите про уровни кешей в hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate использует кэширование, чтобы сделать приложение быстрее. Кэш Hibernate может быть очень полезным в получении
высокой производительности приложения при правильном использовании. Идея кэширования заключается в сокращении количества
запросов к базе данных.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate использует 3 уровня кеширования:
1. Кеш первого уровня (First-level cache);
2. Кеш второго уровня (Second-level cache);
3. Кеш запросов (Query cache);

##### Кэш первого уровня 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Кэш первого уровня Hibernate связан с объектом Session (точнее с persistence context). Кэш первого уровня у Hibernate
включен по умолчанию и не существует никакого способа, чтобы его отключить. Однако Hibernate предоставляет методы,
с помощью которых мы можем удалить выбранные объекты из кэша или полностью очистить кэш. Любой объект закэшированный в
session не будет виден другим объектам session. После закрытия объекта сессии все кэшированные объекты будут потеряны.
При помещении объекта в persistence context, то есть при его загрузке из БД или сохранении, объект так же автоматически
будет помещён в кэш первого уровня. Соответственно, при запросах того же самого объекта несколько раз в рамках одного
persistence context, запрос в БД будет выполнен один раз, а всё остальные загрузки будут выполнены из кэша.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
При загрузке объекта методом load() или объекта с лениво загружаемыми полями, лениво загружаемые данные в кэш не попадут.
При обращении к данным будет выполнен запрос в базу и данные будут загружены и в объект и в кэш. А вот следующая попытка
лениво загрузить объект приведёт к тому, что объект сразу вернут из кэша и уже полностью загруженным.

##### Кэш второго уровня

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Если кэш первого уровня существует только на уровне сессии и persistence context, то кэш второго уровня находится выше —
на уровне SessionFactory и, следовательно, один и тот же кэш доступен одновременно в нескольких persistence context.
Кэш второго уровня требует некоторой настройки и поэтому не включен по умолчанию. Настройка кэша заключается в конфигурировании
реализации кэша и разрешения сущностям быть закэшированными.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
**Конфигурирование кэша**.  Hibernate не реализует сам никакого in-memory сache, а использует существующие реализации кэшей.
Раньше Hibernate самостоятельно поддерживал интерфейс с этими кэшами, но сейчас существует ***JCache*** и корректнее будет использовать
этот интерфейс. Реализаций у ***JCache*** множество, например ***ehcache*** - одна из распространённых.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В первую очередь надо добавить поддержку JCache и ehcache в зависимости:
```xml
<dependencies>
  <dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>${ehcache.version}</version>
  </dependency>
  <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>${hibernate.version}</version>
  </dependency>
  <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-jcache</artifactId>
    <version>${hibernate.version}</version>
  </dependency>
</dependencies>
```
Затем настроить hibernate на использование ehcache для кэширования:
```
<property name="hibernate.cache.region.factory_class">org.hibernate.cache.jcache.JCacheRegionFactory</property>
<property name="hibernate.javax.cache.provider">org.ehcache.jsr107.EhcacheCachingProvider</property>
```
В первой строке мы говорим Hibernate, что хотим использовать JCache интерфейс, а во второй строке выбираем конкретную реализацию JCache: ehcache.

Включим кэш второго уровня:
```xml	
<property name="hibernate.cache.use_second_level_cache">true</property>
```

##### @Cacheable и @Cache

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
***@Cacheable*** это аннотация JPA и позволяет объекту быть закэшированным. Hibernate поддерживает эту аннотацию в том же ключе.
```
@Entity
@Cacheable
public class Person extends AbstractIdentifiableObject {
    @Getter
    @Setter
    private String firstName;
    
    //Other fields
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
***@Cache*** это аннотация Hibernate, настраивающая тонкости кэширования объекта в кэше второго уровня Hibernate. Аннотации @Cacheable 
достаточно, чтобы объект начал кэшироваться с настройками по умолчанию. При этом @Cache использованная без @Cacheable не разрешит
кэширование объекта.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
***@Cache***  принимает три параметра:

* ***include***, имеющий по умолчанию значение all и означающий кэширование всего объекта. Второе возможное значение, non-lazy, запрещает
кэширование лениво загружаемых объектов. Кэш первого уровня не обращает внимания на эту директиву и всегда кэширует лениво загружаемые
объекты.

* ***region*** позволяет задать имя региона кэша для хранения сущности. Регион можно представить как разные кэши или разные части кэша,
имеющие разные настройки на уровне реализации кэша. Например, я мог бы создать в конфигурации ehcache два региона, один с краткосрочным
хранением объектов, другой с долгосрочным и отправлять часто изменяющиеся объекты в первый регион, а все остальные во второй.

* ***usage*** задаёт стратегию одновременного доступа к объектам.
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    Проблема заключается в том, что кэш второго уровня доступен из нескольких сессий сразу и несколько потоков программы могут одновременно
    в разных транзакциях работать с одним и тем же объектом. Следовательно надо как-то обеспечивать их одинаковым представлением этого объекта.
      
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      Стратегий одновременного доступа к объектам в кэше в hibernate существует четыре:
      
      1. ***translactional*** — полноценное разделение транзакций. Каждая сессия и каждая транзакция видят объекты, как если бы
      только они с ним работали последовательно одна транзакция за другой. Плата за это — блокировки и потеря производительности.
      
      2. ***read-write*** — полноценный доступ к одной конкретной записи и разделение её состояния между транзакциями. Однако суммарное
      состояние нескольких объектов в разных транзакциях может отличаться.
      
      3. ***nonstrict-read-write*** — аналогичен read-write, но изменения объектов могут запаздывать и транзакции могут видеть старые
      версии объектов. Рекомендуется использовать в случаях, когда одновременное обновление объектов маловероятно и не может привести
      к проблемам.
            
      4. ***read-only*** — объекты кэшируются только для чтения и изменение удаляет их из кэша.
    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Transactional стратегия самая медленная, read-only самая быстрая. Недостатком read-only стратегии является её бесполезность,
в случае если объекты постоянно изменяются, так как в этом случае они не будут задерживаться в кэше.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Использование кэша второго уровня требует изменений в конфигурации Hibernate и в коде сущностей, но не требует изменения кода запросов
и управления сущностями.

##### Кэш запросов

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Кэши первого и второго уровней работают с объектами загружаемыми по id. Но в реальности к базе чаще выполняются запросы с условиями,
чем загружаются какие-то заранее известные объекты:
```
session.createCriteria(Passport.class)
  .add(Restrictions.eq("series", "AS"))
  .uniqueResult()
session.createCriteria(Passport.class)
  .add(Restrictions.eq("series", "AS"))
  .uniqueResult()
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
И результат выполнения таких запросов тоже может потребоваться кэшировать. Например если вы делаете поисковый сайт по автозапчастям,
то можете кэшировать запросы пользователей, которые, скорее всего, ищут одни запчасти гораздо чаще других. У кэша запросов есть
и своя цена — Hibernate будет вынужден отслеживать сущности закешированные с определённым запросом и выкидывать запрос из кэша,
если кто-то поменяет значение сущности. То есть для кэша запросов стратегия параллельного доступа всегда read-only.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Чтобы включить кэш запросов надо настроить внешний кэш, так же как и для кэша второго уровня, и разрешить Hibernate кэшировать запросы:
```xml
<property name="hibernate.cache.use_query_cache">true</property>
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Но даже с этим разрешением Hibernate не будет кэшировать все запросы, а только те, кэширование которых явно запрошено методом setCacheable()
```
session = sessionFactory.openSession();
session.beginTransaction();
 
// Database will be queried
System.out.println(session.createCriteria(Passport.class)
  .add(Restrictions.eq("series", "AS"))
  .setCacheable(true)
  .uniqueResult());
 
session.getTransaction().commit();
session.close();
 
session = sessionFactory.openSession();
session.beginTransaction();
 
// Database will not be queries, query cache will provide the data
System.out.println(session.createCriteria(Passport.class)
  .add(Restrictions.eq("series", "AS"))
  .setCacheable(true)
  .uniqueResult());
 
session.getTransaction().commit();
session.close();
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Кэш запросов, так же как и кэш второго уровня, существует на уровне SessionFactory и доступен во всех persistence context.

[Оригинальная статья](https://easyjava.ru/data/hibernate/keshirovanie-v-hibernate/)

[к оглавлению](#Hibernate)

---

### 20. Что такое StatelessSessionFactory Зачем он нужен где он используется

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
StatelessSession – командно-ориентированный API, предоставляемый Hibernate.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
С StatelessSession не связан persistence context. Т.о. с ней так же не связана большая часть жизненного цикла  сущности.

StatelessSession не поддерживает:

- кеш первого уровня.
- любой кеш второго уровня или кеш запросов.
- полиморфизм сущностей.
- коллекции.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Вообще это низкоуровневая абстракция, которая ближе к JDBC. Используется для потоковой передачи данных в базу и из
нее в форме отсоединенных (detached) объектов. StatelessSession не имеет ассоциированного persistence-контекста и не предоставляет большую
часть высокоуровневой семантики.
```
StatelessSession session = sessionFactory.openStatelessSession();
Transaction tx = session.beginTransaction();
   
ScrollableResults customers = session.getNamedQuery("GetCustomers")
    .scroll(ScrollMode.FORWARD_ONLY);
while ( customers.next() ) {
    Customer customer = (Customer) customers.get(0);
    customer.updateStuff(...);
    session.update(customer);
}
   
tx.commit();
session.close();
```
[к оглавлению](#Hibernate)

---

### 21. Зачем нужен режим read only

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Могут быть разные причины отмечать транзакции как доступные только для чтения.

- Транзакции для чтения могут выглядеть действительно странно, и часто люди в этом случае не отмечают методы для транзакций.
Но JDBC все равно создаст транзакциюю Или иными словами все методы так или иначе будут происходить внутри танзакции.
Просто она будет работать в autocommit = true, если другой параметр не был установлен явно.

- Если пометить метод как @Transactional (readonly = true), транзакция будет осуществлена в режиме
только для чтения, таким образом, вы укажете, действительно ли возможна запись в БД в рамках этой транзакции.
Если поместить запрос на изменение там, где он не ожидается, этот флаг укажет вам на проблемное место.

- Также БД могут оптимизировать транзакции только для чтения, но это, конечно, зависит от БД. Например. MySQL добавил поддержку этого только в InnoDB,
начиная с версии 5.6.4.


- Если вы не используете JDBC напрямую, а через ORM, то без ***@Transactional*** в таком случае в принципе работать нельзя, это может привести к недетерминированным результатам
и это будет зависеть от драйвера. Hibernate транзакцию откроет, но при выходе из метода, коммитить не будет. И что произойдет, когда соединение вернется в пул с незакомиченой транзакцией -
спецификация не оглашает (т.е. поведение драйвера вариативно). MySQL ее вроде откатывает, Oracle ее коммитит. Если говорить о DataSource, то в C3P0 это поведение настраиваемо.

- Spring устанавливает у Hibernate FlushMode в MANUAL для read-only транзакций, что приводит к другим оптимизациям, таким как отсутствие
необходимости в dirty-reading проверках.

[к оглавлению](#Hibernate)

---

### 22. Назовите некоторые важные аннотации используемые для отображения в Hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate поддерживает как аннотации из JPA, так и свои собственные, которые находятся в пакете org.hibernate.annotations. Наиболее важные аннотации JPA и Hibernate:

* ***@Entity*** *javax.persistence.Entity*: используется для указания класса как entity bean.

* ***@Table*** *javax.persistence.Table*: используется для определения имени таблицы из БД, которая будет отображаться на entity bean.

* ***@Access*** *javax.persistence.Access*: определяет тип доступа, поле или свойство. Поле — является значением по умолчанию и если нужно, чтобы hibernate использовал методы getter/setter, то их необходимо задать для нужного свойства.

* ***@Access*** *javax.persistence.Id*: определяет primary key в entity bean.

* ***@Access*** *javax.persistence.EmbeddedId*: используется для определения составного ключа в бине.

* ***@Column*** *javax.persistence.Column*: определяет имя колонки из таблицы в базе данных.

* ***@GeneratedValue*** *javax.persistence.GeneratedValue*: задает стратегию создания основных ключей. Используется в сочетании с javax.persistence.GenerationType enum.

* ***@OneToOne*** *javax.persistence.OneToOne*: задает связь один-к-одному между двумя сущностными бинами. Соответственно есть другие аннотации OneToMany, ManyToOne и ManyToMany.

* ***@Cascade*** *org.hibernate.annotations.Cascade*: определяет каскадную связь между двумя entity бинами. Используется в связке с org.hibernate.annotations.CascadeType.

* ***@JoinColumn*** *javax.persistence.JoinColumn*: определяет внешний ключ для свойства. Используется вместе с ***@GenericGenerator*** org.hibernate.annotations.GenericGenerator и
***@Parameter*** org.hibernate.annotations.Parameter.

[к оглавлению](#Hibernate)

---

### 23. Как реализованы Join’ы Hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Существует несколько способов реализовать связи в Hibernate.

* Использовать ассоциации, такие как one-to-one, one-to-many, many-to-many.

* Использовать в HQL запросе команду JOIN. Существует другая форма «join fetch«, позволяющая загружать ассоциированные данные.

* Использовать чистый SQL запрос с командой join.

[к оглавлению](#Hibernate)

---

### 24. Почему мы не должны делать Entity class как final

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Хибернейт использует прокси классы для ленивой загрузки данных (т.е. по необходимости, а не сразу). 
Это достигается с помощью расширения entity bean и, следовательно, если бы он был final, то это было бы невозможно. 
Ленивая загрузка данных во многих случаях повышает производительность, а следовательно важна.

[к оглавлению](#Hibernate)

---

### 25. Что такое Named SQL Query

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate поддерживает именованный запрос, который мы можем задать в каком-либо центральном месте и потом использовать его
в любом месте в коде. 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Именованные запросы поддерживают как HQL, так и Native SQL. Создать именованный запрос можно с помощью JPA аннотаций
***@NamedQuery, @NamedNativeQuery*** или в конфигурационном файле отображения (mapping files).

[Hibernate — примеры именованных запросов NamedQuery](http://javastudy.ru/hibernate/hibernate-namedquery/)

[к оглавлению](#Hibernate)

---

### 26. Каковы преимущества Named SQL Query

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Именованный запрос Hibernate позволяет собрать множество запросов в одном месте, а затем вызывать их в любом классе. 
Синтаксис Named Query проверяется при создании session factory, что позволяет заметить ошибку на раннем этапе,
а не при запущенном приложении и выполнении запроса. Named Query глобальные, т.е. заданные однажды, могут быть использованы в любом месте.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Однако одним из основных недостатков именованного запроса является то, что его очень трудно отлаживать 
(могут быть сложности с поиском места определения запроса).

[к оглавлению](#Hibernate)

---

### 27. Расскажите о преимуществах использования Hibernate Criteria API

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate Criteria API является более объектно-ориентированным для запросов, которые получают результат из базы данных. Для операций update, delete или других DDL манипуляций использовать Criteria API нельзя. Критерии используются только для выборки из базы данных в более объектно-ориентированном стиле.

Вот некоторые области применения Criteria API:

* Criteria API поддерживает проекцию, которую мы можем использовать для агрегатных функций вроде sum(), min(), max() и т.д.

* Criteria API может использовать ProjectionList для извлечения данных только из выбранных колонок.

* Criteria API может быть использована для join запросов с помощью соединения нескольких таблиц, используя методы createAlias(), setFetchMode() и setProjection().

* Criteria API поддерживает выборку результатов согласно условиям (ограничениям). Для этого используется метод add() с помощью которого добавляются ограничения (Restrictions).

* Criteria API позволяет добавлять порядок (сортировку) к результату с помощью метода addOrder().

[к оглавлению](#Hibernate)

---

### 28. Как логировать созданные Hibernate SQL запросы в лог файлы

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для логирования запросов SQL добавьте в файл конфигурации Hibernate строчку:
```
<property name="hibernate.show_sql">true</property>
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Отметьте, что это необходимо использовать на уровне Development или Testing и должно быть отключено в продакшн.

[к оглавлению](#Hibernate)

---

### 29. Как управлять транзакциями с помощью Hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Hibernate вообще не допускает большинство операций без использования транзакций. 
Поэтому после получения экземпляра session от SessionFactory необходимо выполнить beginTransaction() для начала транзакции. 
Метод вернет ссылку, которую мы можем использовать для подтверждения или отката транзакции.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В целом, управление транзакциями в фреймворке выполнено гораздо лучше, чем в JDBC, т.к. 
мы не должны полагаться на возникновение исключения для отката транзакции. 
Любое исключение автоматически вызовет rollback.

[к оглавлению](#Hibernate)

---

### 30. Что такое каскадные связи обновления и какие каскадные типы есть в Hibernate

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Если у нас имеются зависимости между сущностями (entities), то нам необходимо определить как различные операции будут влиять на другую сущность. 
Это реализуется с помощью каскадных связей (или обновлений). Вот пример кода с использованием аннотации ***@Cascade***:
```java
import org.hibernate.annotations.Cascade;
 
@Entity
@Table(name = "EMPLOYEE")
public class Employee {
 
@OneToOne(mappedBy = "employee")
@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
private Address address;
 
}
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Есть некоторые различия между enum CascadeType в Hibernate и в JPA. Поэтому обращайте внимание какой пакет вы импортируете при использовании
аннотации и константы типа. Наиболее часто используемые CascadeType перечисления описаны ниже.

* None: без Cascading. Формально это не тип, но если мы не указали каскадной связи, то никакая операция для родителя не будет иметь эффекта для ребенка.

* ALL: Cascades save, delete, update, evict, lock, replicate, merge, persist. В общем — всё.

* SAVE_UPDATE: Cascades save и update. Доступно только для hibernate.

* DELETE: передает в Hibernate native DELETE действие. Только для hibernate.

* DETATCH, MERGE, PERSIST, REFRESH и REMOVE – для простых операций.

* LOCK: передает в Hibernate native LOCK действие.

* REPLICATE: передает в Hibernate native REPLICATE действие.

[к оглавлению](#Hibernate)

---

### 31. Какие паттерны применяются в Hibernate

* Domain Model Pattern – объектная модель предметной области, включающая в себя как поведение так и данные.

* Data Mapper – слой мапперов (Mappers), который передает данные между объектами и базой данных, сохраняя их независимыми друг от друга и себя.

* Proxy Pattern — применяется для ленивой загрузки.

* Factory pattern — используется в SessionFactory

[к оглавлению](#Hibernate)

---

### 32. Расскажите о Hibernate Validator Framework

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Проверка данных является неотъемлемой частью любого приложения. Hibernate Validator обеспечивает эталонную реализацию двух спецификаций
JSR-303 и JSR-349 применяемых в Java. Для настройки валидации в Hibernate необходимо сделать следующие шаги.

Добавить hibernate validation зависимости в проект.
```xml
<dependencies>
    <dependency>
        <groupId>javax.validation</groupId>
        <artifactId>validation-api</artifactId>
        <version>1.1.0.Final</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>5.1.1.Final</version>
    </dependency>
</dependencies>

```
Так же требуются зависимости из JSR 341, 
реализующие Unified Expression Language для обработки динамических выражений и сообщений о нарушении ограничений.
```xml
<dependencies>
    <dependency>
        <groupId>javax.el</groupId>
        <artifactId>javax.el-api</artifactId>
        <version>2.2.4</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish.web</groupId>
        <artifactId>javax.el</artifactId>
        <version>2.2.4</version>
    </dependency>
</dependencies>
```
Использовать необходимые аннотации в бинах.
```java
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
 
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
 
public class Employee {
 
    @Min(value=1, groups=EmpIdCheck.class)
    private int id;
     
    @NotNull(message="Name cannot be null")
    @Size(min=5, max=30)
    private String name;
     
    @Email
    private String email;
     
    @CreditCardNumber
    private String creditCardNumber;
}
```
        
[к оглавлению](#Hibernate)

---

### 33. Best Practices в Hibernate

При использовании фреймворка Hibernate рекомендуется придерживаться некоторых правил.

* Всегда проверяйте доступ к primary key. Если он создается базой данных, то вы не должны иметь сеттера.

* По умолчанию hibernate устанавливает значения в поля напрямую без использования сеттеров. Если необходимо заставить хибернейт их применять, то проверьте использование аннотации @Access(value=AccessType.PROPERTY) над свойством.

* Если тип доступа — property, то удостоверьтесь, что аннотация используется с геттером. Избегайте смешивания использования аннотации над обоими полями и геттером.

* Используйте нативный sql запрос только там, где нельзя использовать HQL.

* Используйте ordered list вместо сортированного списка из Collection API, если вам необходимо получить отсортированные данные.

* Применяйте именованные запросы разумно — держите их в одном месте и используйте только для часто применяющихся запросов. Для специфичных запросов пишите их внутри конкретного бина.

* В веб приложениях используйте JNDI DataSource вместо файла конфигурации для соединения с БД.

* Избегайте отношений многие-ко-многим, т.к. это можно заменить двунаправленной One-to-Many и Many-to-One связью.

* Для collections попробуйте использовать Lists, maps и sets. Избегайте массивов (array), т.к. они не дают преимуществ ленивой загрузки.

* Не обрабатывайте исключения, которые могут откатить транзакцию и закрыть сессию. Если это проигнорировать, то Hibernate не сможет гарантировать, что состояние в памяти соответствует состоянию персистентности (могут быть коллизии данных).

* Применяйте шаблон DAO для методов, которые могут использоваться в entity бинах.

* Предпочитайте ленивую выборку для ассоциаций.

[к оглавлению](#Hibernate)

---
