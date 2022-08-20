package org.techtown.HowAboutThisDay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder>{
    ArrayList<commentList_item> commentList = new ArrayList<commentList_item>();

    public interface OnItemClickListener{
        void onDeleteClick(View view, int position);
    }
    private OnItemClickListener Listener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.Listener = listener;
    }

    @NonNull
    @Override
    public Comment_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_comment, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Comment_Adapter.ViewHolder holder, int position){
        holder.onBind(commentList.get(position));
    }
    void setCommentList(ArrayList<commentList_item> list){
        this.commentList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return commentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView commentView, userView;
        Button delete_comment = itemView.findViewById(R.id.delete_comment);


        public ViewHolder(View itemView){
            super(itemView);

            delete_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        if (Listener != null){
                            Listener.onDeleteClick(view, position);
                        }
                    }
                }
            });

            commentView = itemView.findViewById(R.id.commentView);
            userView = itemView.findViewById(R.id.userView);

        }
        void onBind(commentList_item item){
            commentView.setText(item.getComment());
            userView.setText(item.getUser());
        }

        public TextView getCommentView() {
            return commentView;
        }
    }
}
