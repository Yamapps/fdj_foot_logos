package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.Team

object DataProvider {

    private fun league(index: Int)= League("$index", "League $index", "Sport $index", "Alternate $index")

    fun leagues(size: Int): List<League>{
        val list = mutableListOf<League>()
        for(i in 0..<size){
            list.add(league(i))
        }

        return list
    }

    private fun team(index: Int)= Team("$index", "https://www.thesportsdb.com/images/media/team/badge/z69be41598797026.png", "name $index")

    fun teams(size: Int): List<Team>{
        val list = mutableListOf<Team>()
        for(i in 0..<size){
            list.add(team(i))
        }

        return list
    }
}