package com.newsapiclient.client.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.newsapiclient.R;
import com.newsapiclient.model.Article;

import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
        return new ArticleViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        final ArticleViewHolder articleViewHolder = holder;
        Article article = articles.get(position);

        // Glide request options
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        articleViewHolder.mProgressLoad.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        articleViewHolder.mProgressLoad.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(articleViewHolder.mArticleImg);

        articleViewHolder.mTitle.setText(article.getTitle());
        articleViewHolder.mDescription.setText(article.getDescription());
        articleViewHolder.mAuthor.setText(article.getAuthor());
        // TODO: 9/11/19 Create converter for this textview
        articleViewHolder.mPublishedAt.setText(article.getPublishedAt());
        articleViewHolder.mSource.setText(article.getSource().getName());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mDescription;
        TextView mAuthor;
        TextView mPublishedAt;
        TextView mSource;
        ImageView mArticleImg;
        ProgressBar mProgressLoad;
        OnItemClickListener onItemClickListener;

        public ArticleViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);

            mTitle = itemView.findViewById(R.id.article_title);
            mDescription = itemView.findViewById(R.id.article_description);
            mAuthor = itemView.findViewById(R.id.article_author);
            mPublishedAt = itemView.findViewById(R.id.published_at);
            mSource = itemView.findViewById(R.id.article_source);
            mArticleImg = itemView.findViewById(R.id.article_img);
            mProgressLoad = itemView.findViewById(R.id.progress_load_img);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
