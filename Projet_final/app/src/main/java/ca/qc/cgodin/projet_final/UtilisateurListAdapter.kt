package ca.qc.cgodin.projet_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UtilisateurListAdapter constructor(
    context: Context
) : RecyclerView.Adapter<UtilisateurListAdapter.UtilisateurViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var utilisateurs = emptyList<Utilisateur>() // Cached copy of students
    inner class UtilisateurViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val fullNameItemView: TextView =
            itemView.findViewById(R.id.usernameView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            UtilisateurViewHolder {
        val itemView = inflater.inflate(R.layout.recycler_view_item, parent,
            false)
        return UtilisateurViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: UtilisateurViewHolder, position: Int) {
        val current = utilisateurs[position]
        holder.fullNameItemView.text = "${current.username} ${current.password}"
    }
    fun setUtilisateur(students: List<Utilisateur>) {
        this.utilisateurs = students
        notifyDataSetChanged()
    }
    override fun getItemCount() = utilisateurs.size
}
