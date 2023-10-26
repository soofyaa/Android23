package com.itis.android23.util

import com.itis.android23.model.Answer
import com.itis.android23.model.Question

object QuestionsRepository {

    val allQuestions: List<Question> = listOf(
        Question("Какой язык программирования вы предпочитаете?", listOf(
            Answer("Python"),
            Answer("Java"),
            Answer("JavaScript"),
            Answer("C++"),
            Answer("Kotlin"),
            Answer("Ruby"),
            Answer("C#"),
            Answer("Go"),
            Answer("Swift"),
            Answer("Rust")
        )),
        Question("Какую операционную систему вы предпочитаете?", listOf(
            Answer("Windows"),
            Answer("MacOS"),
            Answer("Linux"),
            Answer("iOS"),
            Answer("Android"),
            Answer("Ubuntu"),
            Answer("Chrome OS"),
            Answer("FreeBSD"),
            Answer("Solaris"),
            Answer("Unix")
        )),
        Question("Какой ваш любимый цвет?", listOf(
            Answer("Синий"),
            Answer("Красный"),
            Answer("Зеленый"),
            Answer("Желтый"),
            Answer("Розовый"),
            Answer("Фиолетовый"),
            Answer("Оранжевый"),
            Answer("Черный"),
            Answer("Белый"),
            Answer("Коричневый")
        )),
        Question("Что вы предпочитаете: кофе или чай?", listOf(
            Answer("Кофе"),
            Answer("Чай"),
            Answer("Какао"),
            Answer("Сок"),
            Answer("Вода")
        )),
        Question("Какой ваш любимый цветок?", listOf(
            Answer("Роза"),
            Answer("Тюльпан"),
            Answer("Лилия"),
            Answer("Орхидея"),
            Answer("Пион"),
            Answer("Нарцисс"),
            Answer("Гиацинт"),
            Answer("Амарилис"),
            Answer("Крокус")
        )),
        Question("Что вы предпочитаете: горы или море?", listOf(
            Answer("Горы"),
            Answer("Море"),
            Answer("Пустыня"),
            Answer("Лес"),
            Answer("Поле")
        )),
        Question("Сколько вам лет?", listOf(
            Answer("Меньше 18"),
            Answer("18-24"),
            Answer("25-34"),
            Answer("35-44"),
            Answer("Больше 45"),
            Answer("55-64"),
            Answer("65-74"),
            Answer("75-84"),
            Answer("85-94"),
            Answer("Больше 95")
        )),
        Question("Когда ходили в кино?", listOf(
            Answer("На прошлой неделе"),
            Answer("Месяц назад"),
            Answer("Больше месяца назад"),
            Answer("Никогда не хожу в кино"),
            Answer("Был недавно, но не помню точно")
        )),
        Question("Какой ваш любимый фильм?", listOf(
            Answer("Звездные войны"),
            Answer("Властелин колец"),
            Answer("Гарри Поттер"),
            Answer("Титаник"),
            Answer("Побег из Шоушенка"),
            Answer("Аватар"),
            Answer("Матрица"),
            Answer("Хоббит"),
            Answer("Игры разума"),
            Answer("Форрест Гамп")
        )),
        Question("Какая у вас любимая порода кошек?", listOf(
            Answer("Мейнкун"),
            Answer("Сиамская"),
            Answer("Британская"),
            Answer("Персидская"),
            Answer("Скоттиш-страйт"),
            Answer("Сфинкс"),
            Answer("Манкс"),
            Answer("Сибирская"),
            Answer("Русская голубая"),
            Answer("Бирманская")
        ))
    )

    fun getQuestionByPosition(position: Int): Question {
        return if (position in allQuestions.indices) {
            allQuestions[position]
        } else {
            throw Exception()
        }
    }
}