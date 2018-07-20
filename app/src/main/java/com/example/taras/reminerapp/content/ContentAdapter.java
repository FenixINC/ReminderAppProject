package com.example.taras.reminerapp.content;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.taras.reminerapp.BR;
import com.example.taras.reminerapp.R;
import com.example.taras.reminerapp.databinding.ItemEventBinding;
import com.example.taras.reminerapp.databinding.ItemNewsBinding;
import com.example.taras.reminerapp.databinding.ItemVideoBinding;
import com.example.taras.reminerapp.db.model.Event;
import com.example.taras.reminerapp.db.model.News;
import com.example.taras.reminerapp.db.model.Remind;
import com.example.taras.reminerapp.db.model.Video;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Taras Koloshmatin on 20.07.2018
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder<Remind, ViewDataBinding>> {

    private List<Remind> mList = new ArrayList<>();
    private OnRemindClickListener mListener;

    public ContentAdapter(OnRemindClickListener listener) {
        mListener = listener;
    }

    public void setList(List<? extends Remind> list) {
        if (list == null) {
            Timber.d("Null list");
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public Remind getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder<Remind, ViewDataBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder;
        switch (viewType) {
            case R.layout.item_news:
                viewHolder = new NewsViewHolder(ItemNewsBinding.inflate(inflater, parent, false));
                break;
            case R.layout.item_event:
                viewHolder = new EventViewHolder(ItemEventBinding.inflate(inflater, parent, false));
                break;
            case R.layout.item_video:
                viewHolder = new VideoViewHolder(ItemVideoBinding.inflate(inflater, parent, false));
                break;
            default:
                viewHolder = new NewsViewHolder(ItemNewsBinding.inflate(inflater, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Remind, ViewDataBinding> holder, int position) {
        holder.binding.setVariable(BR.model, getItem(position));
        holder.binding.setVariable(BR.clickListener, mListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Remind model = mList.get(position);
        if (model instanceof News) {
            return R.layout.item_news;
        } else if (model instanceof Video) {
            return R.layout.item_video;
        } else if (model instanceof Event) {
            return R.layout.item_event;
        } else {
            return 0;
        }
    }

    abstract class ViewHolder<T extends Remind, K extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public K binding;

        public ViewHolder(K binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class NewsViewHolder extends ViewHolder<News, ItemNewsBinding> {

        public NewsViewHolder(ItemNewsBinding binding) {
            super(binding);
        }
    }

    class EventViewHolder extends ViewHolder<Event, ItemEventBinding> {

        public EventViewHolder(ItemEventBinding binding) {
            super(binding);
        }
    }

    class VideoViewHolder extends ViewHolder<Video, ItemVideoBinding> {

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding);
        }
    }
}
