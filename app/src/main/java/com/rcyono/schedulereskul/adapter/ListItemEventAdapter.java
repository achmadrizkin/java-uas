package com.rcyono.schedulereskul.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rcyono.schedulereskul.R;
import com.rcyono.schedulereskul.model.event.Event;
import com.rcyono.schedulereskul.ui.collection.CollectionFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class ListItemEventAdapter extends RecyclerView.Adapter<ListItemEventAdapter.ListItemEventViewHolder> {
    private final ArrayList<Event> listEvent = new ArrayList<>();

    public void setAdapter(List<Event> events) {
        listEvent.addAll(events);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ListItemEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_event, parent, false);
        return new ListItemEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemEventViewHolder holder, int position) {
        holder.bind(listEvent.get(position));
        holder.itemView.setOnClickListener(v -> {
            CollectionFragmentDirections.ActionCollectionFragmentToDetailEventFragment toDetailEvent = CollectionFragmentDirections.actionCollectionFragmentToDetailEventFragment();
            toDetailEvent.setEvent(listEvent.get(position));
            Navigation.findNavController(holder.itemView).navigate(toDetailEvent);
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public static class ListItemEventViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        public ListItemEventViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_event);
        }

        public void bind(Event event) {
            Log.d("TAG", "bind: " + event.getImagePath());
            Glide.with(itemView.getContext())
                    .load(event.getImagePath())
                    .into(ivAvatar);
        }
    }
}
