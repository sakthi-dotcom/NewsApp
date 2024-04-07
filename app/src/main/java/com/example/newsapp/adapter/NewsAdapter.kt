
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article

class NewsAdapter( var articles: List<Article>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.titleTextView.text = article.title
            binding.linkTextView.text = article.url
            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.error)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(article.url)
            }
        }
    }
}
