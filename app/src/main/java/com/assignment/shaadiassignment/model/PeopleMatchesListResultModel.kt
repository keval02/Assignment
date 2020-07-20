package com.assignment.shaadiassignment.model

import java.io.Serializable

class PeopleMatchesListResultModel() : Serializable {
    val cell: String = ""
    val dob: Dob = Dob()
    val email: String = ""
    val gender: String = ""
    val location: Location = Location()
    val name: Name = Name()
    val picture: Picture = Picture()
    var reviewedUser : Int = 0

    class Dob {
        val age: Int = 0
    }

    class Location() {
        val city: String = ""
        val country: String = ""
        val state: String = ""
    }


    class Name {
        val first: String = ""
        val last: String = ""
    }

    class Picture {
        val large: String = ""
    }
}

