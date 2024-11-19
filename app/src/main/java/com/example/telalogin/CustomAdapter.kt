import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telalogin.R

class CustomAdapter(private val dataset: Array<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // ViewHolder para manter referência às views do layout do item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_text)
    }

    // Cria novas views (chamado pelo layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false) // item_layout é o layout do item do RecyclerView
        return ViewHolder(view)
    }

    // Substitui o conteúdo de uma view (chamado pelo layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataset[position]
    }

    // Retorna o tamanho do dataset (chamado pelo layout manager)
    override fun getItemCount() = dataset.size
}
