package com.assignment.shaadiassignment.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.shaadiassignment.R
import com.assignment.shaadiassignment.adapter.PeopleMatchesAdapter
import com.assignment.shaadiassignment.apis.APIGenerator
import com.assignment.shaadiassignment.apis.RetrofitAPIInterface
import com.assignment.shaadiassignment.model.PeopleMatchesListModel
import com.assignment.shaadiassignment.model.PeopleMatchesListResultModel
import com.assignment.shaadiassignment.utilities.AppGlobal
import com.assignment.shaadiassignment.utilities.SharedPreferenceHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_people_matches.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleMatchesActivity : AppCompatActivity() {
    val TAG: String = PeopleMatchesActivity::class.java.getName()
    lateinit var progressDialog: Dialog
    lateinit var retrofitAPIInterface: RetrofitAPIInterface
    var totalResults: Int = 10
    var matchedPeopleList: ArrayList<PeopleMatchesListResultModel> = ArrayList()
    lateinit var peopleMatchesAdapter: PeopleMatchesAdapter
    lateinit var preference: SharedPreferenceHelper
    var storedMatchedPeopleData: PeopleMatchesListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_matches)
        retrofitAPIInterface = APIGenerator.apiClass
        preference = SharedPreferenceHelper(applicationContext)
        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.custom_dialog_progress)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)

        if (AppGlobal.getMatchedPeopleData(preference) != null) {
            storedMatchedPeopleData = AppGlobal.getMatchedPeopleData(preference)
        }


        peopleMatchesAdapter = object : PeopleMatchesAdapter(this, matchedPeopleList) {
            override fun updateMemberResponse(peopleList: ArrayList<PeopleMatchesListResultModel>) {
                val matchesList = PeopleMatchesListModel()
                matchesList.results.addAll(peopleList)
                AppGlobal.setMatchedPeopleData(matchesList, preference)
            }

        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_people_list.layoutManager = layoutManager
        rv_people_list.adapter = peopleMatchesAdapter

        if (storedMatchedPeopleData != null && storedMatchedPeopleData!!.results.size != 0) {
            storedMatchedPeopleData?.results.let {
                matchedPeopleList.addAll(it!!)
            }

        } else {
            fetchPeopleList(totalResults)
        }

    }

    /**
     * FUNCTION COMMENT
     *
     * The function will call api of people when there is no data available in our system.
     * @param totalResults is used as query params to get that number of results from api.
     * @return function will return the list of people with its details.
     */
    private fun fetchPeopleList(totalResults: Int) {
        progressDialog.show()
        val apiResponseCall: Call<PeopleMatchesListModel> =
            retrofitAPIInterface.getAllMatchedPeople(totalResults.toString())
        apiResponseCall.enqueue(object : Callback<PeopleMatchesListModel> {
            override fun onFailure(call: Call<PeopleMatchesListModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@PeopleMatchesActivity,
                    getString(R.string.message_something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<PeopleMatchesListModel>,
                response: Response<PeopleMatchesListModel>
            ) {
                progressDialog.dismiss()
                try {
                    val matchesList = response.body() ?: PeopleMatchesListModel()
                    matchesList.let { matchedPeopleList.addAll(it.results) }
                    peopleMatchesAdapter.notifyDataSetChanged()
                    AppGlobal.setMatchedPeopleData(matchesList, preference)
                } catch (exception: Exception) {
                    Log.e(TAG, exception.toString())
                }
            }

        })
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}