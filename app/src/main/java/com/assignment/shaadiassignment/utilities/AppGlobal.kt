package com.assignment.shaadiassignment.utilities

import com.assignment.shaadiassignment.model.PeopleMatchesListModel

object AppGlobal {
    const val USER_STRING_FEMALE = "female"
    var matchedPeopleData = "peopleData"

    fun setMatchedPeopleData(peopleData: PeopleMatchesListModel, sharedPreferenceHelper : SharedPreferenceHelper) {
        sharedPreferenceHelper.setObject(matchedPeopleData, peopleData)
    }

    fun getMatchedPeopleData(sharedPreferenceHelper: SharedPreferenceHelper): PeopleMatchesListModel? {
        return sharedPreferenceHelper.getObject(matchedPeopleData, PeopleMatchesListModel::class.java)
    }
}