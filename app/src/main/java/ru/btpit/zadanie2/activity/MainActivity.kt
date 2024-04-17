package ru.btpit.zadanie2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import ru.btpit.zadanie2.R
import ru.btpit.zadanie2.databinding.ActivityPostsBinding
import ru.btpit.zadanie2.dto.Post
import ru.btpit.zadanie2.util.AndroidUtils
import ru.btpit.zadanie2.viewmodel.PostViewModel



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()

        val adapter = Posts(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.share(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.contentPost) {
                requestFocus()
                setText(post.content)
            }
        }

        // Обработчик нажатия на кнопку share


//        val adapterr = posts {
//            viewModel.share(it.id)
//        }
//        binding.list.adapter = adapterr
//        viewModel.data.observe(this) { posts ->
//            adapter.submitList(posts)
//        }

//        val viewModel: PostViewModel by viewModels()
//        viewModel.data.observe(this) { post ->
//            post.map {post ->
//                binding.container.removeAllViews()
//                ActivityMainBinding.inflate(layoutInflater, binding., true).apply{
//                    author.text = post.author
//                    published.text = post.published
//                    content.text = post.content
//                    val formattedLike = formatNumber(post.likecount)
//                    textView.text = formattedLike
//                    val formattedShare = formatNumber1(post.share)
//                    textView2.text = formattedShare
//
//
//                    like.setImageResource(
//                        if (post.isLiked) R.mipmap.like2 else R.mipmap.like
//
//                    )
//                    like.setOnClickListener {
//
//                        viewModel.like(post.id)
//
//
//                    }
//                    share.setOnClickListener {
//
//
//                        viewModel.share(post.id)
//                    }
//                }.root
//
//            }
//        }

        binding.save.setOnClickListener {
            with(binding.contentPost) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.errorAdd),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
    }
}



