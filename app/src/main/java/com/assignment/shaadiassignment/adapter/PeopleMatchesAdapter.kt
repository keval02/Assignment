package com.assignment.shaadiassignment.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.shaadiassignment.R
import com.assignment.shaadiassignment.model.PeopleMatchesListResultModel
import com.assignment.shaadiassignment.utilities.AppGlobal
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_people_matches_items.view.*

abstract class PeopleMatchesAdapter(
    var activity: Activity,
    var peopleList: ArrayList<PeopleMatchesListResultModel>
) : RecyclerView.Adapter<PeopleMatchesAdapter.PeopleMatchesListViewHolder>() {

    class PeopleMatchesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleMatchesListViewHolder {
        val itemView =
            activity.layoutInflater.inflate(R.layout.layout_people_matches_items, parent, false)
        return PeopleMatchesListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    override fun onBindViewHolder(holder: PeopleMatchesListViewHolder, position: Int) {
        val placeHolderDrawable: Drawable
        val userName: String =
            peopleList[position].name.first + " " + peopleList[position].name.last
        val userOtherDetails: String =
             peopleList[position].location.city + ", " + peopleList[position].location.state + ", " + peopleList[position].location.country
        val userDetails : String = peopleList[position].dob.age.toString() + ", " +peopleList[position].email + ", " + peopleList[position].cell
        val reviewedUser: Int = peopleList[position].reviewedUser
        placeHolderDrawable = if (peopleList[position].gender == AppGlobal.USER_STRING_FEMALE) {
            activity.getDrawable(R.drawable.ic_female_user)!!
        } else {
            activity.getDrawable(R.drawable.ic_male_user)!!
        }

        Glide.with(activity).load(peopleList[position].picture.large)
            .placeholder(placeHolderDrawable)
            .error(placeHolderDrawable)
            .into(holder.itemView.profile_image)

        holder.itemView.userNameTextView.text = userName
        holder.itemView.detailsTextView.text = userDetails
        holder.itemView.otherDetailsTextView.text = userOtherDetails


        when (reviewedUser) {
            0 -> {
                holder.itemView.layoutUserAction.visibility = View.VISIBLE
                holder.itemView.txtActionMessage.visibility = View.GONE
            }

            1 -> {
                holder.itemView.layoutUserAction.visibility = View.GONE
                holder.itemView.txtActionMessage.visibility = View.VISIBLE
                holder.itemView.txtActionMessage.setBackgroundColor(Color.parseColor("#00af80"))
                holder.itemView.txtActionMessage.text =
                    activity.getString(R.string.message_user_accepted)
            }

            2 -> {
                holder.itemView.layoutUserAction.visibility = View.GONE
                holder.itemView.txtActionMessage.visibility = View.VISIBLE
                holder.itemView.txtActionMessage.setBackgroundColor(Color.parseColor("#FE5A61"))
                holder.itemView.txtActionMessage.text =
                    activity.getString(R.string.message_user_decline)
            }
        }

        holder.itemView.layoutUserAccept.setOnClickListener {
            peopleList[position].reviewedUser = 1
            notifyItemChanged(position)
            updateMemberResponse(peopleList)
        }

        holder.itemView.layoutUserDecline.setOnClickListener {
            peopleList[position].reviewedUser = 2
            notifyItemChanged(position)
            updateMemberResponse(peopleList)
        }
    }

    abstract fun updateMemberResponse(peopleList: ArrayList<PeopleMatchesListResultModel>)
}