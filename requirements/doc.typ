#set text(lang: "ru", size: 11pt, font: "New Computer Modern")
#set page(paper: "a4", numbering: "1")
#set par(justify: true)
#set heading(numbering: "1.1")

#align(center, text(16pt)[Система анкетирования студентов])
#align(center, [
  #text(20pt)[*Требования*] \
  Обновлено #datetime.today().display("[day].[month].[year]")
 ])

= Описание
Веб-приложение, которое позволяет проводить 
анонимное анкетирование студентов.

= Типы пользователей
- Создатель анкет
- Аноним

= Функциональность, доступная создателю анкет
== Аутентификация
Аутентификация через Яндекс.
Доступ к функциональности через белый список.

== Конструктор анкет
Создатель анкеты указывает:
- Название
- Дата и время остановки сбора ответов
- Список блоков (см. ниже)

=== Блоки
Для каждого блока указывается, обязателен ли ответ.

1. Текст – пояснение к анкете
2. #text[
  Ответ в свободной форме
  - Текст вопроса
  - Минимальная и максимальная длина текста ответа
]
3. #text[
  Множественный выбор (чекбоксы)
  - Текст вопроса
  - Варианты ответа
  - Минимальное и максимальное число выбранных вариантов ответа (при необходимости)
  - Поле "другое" (при необходимости)
]
4. #text[
  Одиночный выбор (радиокнопка)
  - Текст вопроса
  - Варианты ответа
  - Поле "другое" (при необходимости)
]
5. #text[
  Анкета полярных профилей
  - Текст вопроса
  - Диапазон и шаг шкалы
  - Вербальное описание каждого значения (при необходимости)
]

== Конструктор отчётов
Конструктор отчётов позволяет задавать структуру отчёта
с помощью блоков.

При необходимости ответы в блоках можно сгруппировать
по ответу на другой вопрос.

=== Блоки
1. Ответы в свободной форме – список всех ответов
2. Ответы в свободной форме – анализ и обобщение посредством применения алгоритмов машинного обучения
3. Множественный выбор
   - Столбчатая диаграмма
   - Стандартизированный текст
4. Одиночный выбор
   - Круговая диаграмма
   - Стандартизированный текст
5. Анкета полярных профилей
   - Круговая диаграмма
   - Стандартизированный текст

=== Дробление отчётов
При необходимости отчёт можно разделить на несколько,
в которых информация будет отфильтрована по ответам на выбранный блок анкеты. 

== Дашборд

Пользователю предоставляется доступ ко своим анкетам и анкетам, которыми поделились с этим пользователем.

=== Управление анкетами
Возможности:
- Делиться доступом к администрированию анкеты с помощью почтового адреса
- Приостановка и возобновление сбора ответов

=== Экспорт ответов
Собранные ответы экспортируются в свободном формате XLSX в соответствии со стандартом ECMA-376.

=== Экспорт отчётов
Отчёты экспортируются в свободном формате DOCX в соответствии со стандартом ECMA-376.

= Функциональность, доступная анониму
Если у анонимного пользователя есть ссылка на активную анкету, он может:
- Просматривать её
- Отправить к ней ответ, если все блоки заполнены корректно (неотправленные ответы автоматически сохраняются в браузере)

== Защита от двойной отправки ответов
При отправке второго ответа из одного браузера заменяется старый ответ.