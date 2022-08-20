package org.techtown.HowAboutThisDay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    ArrayList<planList_item> planList = new ArrayList<planList_item>();

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    private OnItemClickListener Listener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.Listener = listener;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_content, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position){
        holder.onBind(planList.get(position));
    }
    void setPlanList(ArrayList<planList_item> list){
        this.planList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return planList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView, userView, dateView;

        public ViewHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    System.out.println(position);
                    if (position != RecyclerView.NO_POSITION){
                        Listener.onItemClick(view, position);
                    }
                }
            });
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            userView = (TextView) itemView.findViewById(R.id.userView);
            dateView = (TextView) itemView.findViewById(R.id.dateView);

        }
        void onBind(planList_item item){
            titleView.setText(item.getTitle());
            userView.setText(item.getUser());
            dateView.setText(item.getDate());
        }

        public TextView getTitleView() {
            return titleView;
        }
    }

}

