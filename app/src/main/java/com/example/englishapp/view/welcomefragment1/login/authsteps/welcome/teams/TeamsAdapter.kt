package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.teams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.TeamsItemBinding
import com.example.englishapp.model.data.createauthprofile.Teams
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener

class TeamsAdapter(private val elementClickLListener: ElementOnFragmentClickLListener) :
    RecyclerView.Adapter<TeamsAdapter.TeamsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.teams_item, parent, false)
        return TeamsHolder(view)
    }

    override fun onBindViewHolder(holder: TeamsHolder, position: Int) {
        val elem = InformationForWelcome.getTeams()[position]
        holder.bind(elem)
        with(holder) {
            binding.root.tag = true
            binding.root.setOnClickListener {
                if (binding.root.tag == true) {
                    binding.root.tag = false
                    binding.teamsName.setTextColor(
                        itemView.resources.getColor(
                            R.color.green,
                            itemView.context.theme
                        )
                    )
                    binding.teamsChecking.setImageResource(R.drawable.ic_teams_checked)
                    elementClickLListener.onElementClick(elem.id.toString())
                } else {
                    binding.root.tag = true
                    binding.teamsName.setTextColor(
                        itemView.resources.getColor(
                            R.color.black,
                            itemView.context.theme
                        )

                    )
                    binding.teamsChecking.setImageResource(R.drawable.ic_teams_unheck)
                }
            }
        }

    }

    override fun getItemCount(): Int = InformationForWelcome.getTeams().size


    inner class TeamsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TeamsItemBinding.bind(itemView)
        fun bind(teams: Teams) {
            binding.teamsImage.setImageResource(teams.images)
            binding.teamsName.text = teams.topic

        }
    }
}