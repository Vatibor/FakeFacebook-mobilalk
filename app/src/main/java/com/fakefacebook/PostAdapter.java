package com.fakefacebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable {
    private ArrayList<PostModel> postModelsArrayList;
    private ArrayList<PostModel> postModelsArrayListAll;
    private Context context;
    int lastPos = -1;
    private PostClickInterface postClickInterface;

    public PostAdapter(ArrayList<PostModel> postModelsArrayList, Context context, PostClickInterface postClickInterface) {
        this.postModelsArrayList = postModelsArrayList;
        this.postModelsArrayListAll = postModelsArrayList;
        this.context = context;
        this.postClickInterface = postClickInterface;
    }

    public PostAdapter(ArrayList<PostModel> postModelsArrayList, Context context) {
        this.postModelsArrayList = postModelsArrayList;
        this.postModelsArrayListAll = postModelsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_rv_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        PostModel postModel = postModelsArrayList.get(position);
        holder.userNameTV.setText(postModel.getUserName());
        holder.dateTV.setText(postModel.getDateTime());
        holder.postTextTV.setText(postModel.getTextField());
        Picasso.get().load(postModel.getImageUrlField()).into(holder.postIV);
        setAninmation(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickInterface.onPostClick(holder.getAdapterPosition());//position);
            }
        });
    }

    private void setAninmation(View itemView, int position){
        if (position > lastPos) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return postModelsArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return postFilter;
    }

    private Filter postFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<PostModel> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = postModelsArrayListAll.size();
                results.values = postModelsArrayListAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PostModel item : postModelsArrayListAll){
                    if(item.getTextField().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            postModelsArrayList = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTV, dateTV, postTextTV;
        private ImageView postIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.idTVuserName);
            dateTV = itemView.findViewById(R.id.idTVdate);
            postTextTV = itemView.findViewById(R.id.idTVpostText);
            postIV = itemView.findViewById(R.id.idIVPost);

        }
    }

    public interface PostClickInterface {
        void onPostClick(int position);
    }

}
