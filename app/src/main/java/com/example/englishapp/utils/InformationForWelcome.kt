package com.example.englishapp.utils

import com.example.englishapp.R
import com.example.englishapp.model.data.createauthprofile.Avatar
import com.example.englishapp.model.data.createauthprofile.Motivation
import com.example.englishapp.model.data.createauthprofile.Teams

object InformationForWelcome {

    fun getMotivation() = listOf(
        Motivation(R.drawable.ic_motivation_air,"Путешествовать","Бронировать билеты, заказывать еду в кафе"),
        Motivation(R.drawable.ic_motivation_kreslo,"Культура","Смотреть фильмы и читать книги"),
        Motivation(R.drawable.ic_motivation_sumka,"Карьера","Работать в классной международной компании"),
        Motivation(R.drawable.ic_motivation_dev,"Саморазвитие","Люблю учиться новому"),
        Motivation(R.drawable.ic_motivation_truba,"Развлечение","Хочу с пользой проводить время"),
        Motivation(R.drawable.ic_motivation_heart,"Для отношений","Найти свою любовь"),
        Motivation(R.drawable.ic_motivation_for_friend,"Для друзей и семьи","Знакомиться с новыми людьми или семьей"),
        Motivation(R.drawable.ic_motivation_other,"Другая","Мой путь уникален")

    )

    fun getTeams() = listOf(
        Teams(R.drawable.ic_teams_air,"Путешествия",1),
        Teams(R.drawable.ic_food,"Еда",3),
        Teams(R.drawable.ic_teams_city,"В городе",4),
        Teams(R.drawable.ic_teams_hearts,"Отношения",5),
        Teams(R.drawable.ic_teams_truba,"Свободное время",2),
        Teams(R.drawable.ic_teams_shoping,"Шопинг",7),
        Teams(R.drawable.ic_teams_career,"Работа",11),
        Teams(R.drawable.ic_team_house,"Дом",8),
        Teams(R.drawable.ic_teams_people,"Люди",9),
        Teams(R.drawable.ic_teams_nature,"Природа и животные",10),
        Teams(R.drawable.ic_teams_other,"Другая",6)

    )

    fun getAvatar() = listOf(
        Avatar(R.drawable.avatar1_base_max,"base_max"),
        Avatar(R.drawable.avatar2_hipster,"hipster"),
        Avatar(R.drawable.avatar3_johnny_depp,"johnny_depp"),
        Avatar(R.drawable.avatar4_rocker,"rocker"),
        Avatar(R.drawable.avatar5_woman_britain,"woman_britain"),
        Avatar(R.drawable.avatar6_woman_latino,"woman_latino")
    )





}