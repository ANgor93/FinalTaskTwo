#language: ru
@TEST
Функция: Создание пользователя

  Сценарий: Создание пользователя
    Дано У меня есть запрос на создание пользователя
    Когда Я отправляю POST запрос на endpoint "/users"
    Тогда Тело ответа должно содержать имя "Tomato" и работу "Eat market" созданного пользователя

