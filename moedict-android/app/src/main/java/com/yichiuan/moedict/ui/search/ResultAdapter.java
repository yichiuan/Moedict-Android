package com.yichiuan.moedict.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.ui.main.MainActivity;

import java.util.ArrayList;

import moe.Index;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>
        implements View.OnClickListener {

    private LayoutInflater inflater;

    private Index index;

    private ArrayList<Integer> results;

    ResultAdapter(Context context, Index index) {
        inflater = LayoutInflater.from(context);
        this.index = index;
    }

    void setResults(ArrayList<Integer> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    ArrayList<Integer> getResults() {
        return results;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultViewHolder holder = new ResultViewHolder(
                inflater.inflate(R.layout.item_search_result, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        String word = index.words(results.get(position));
        holder.item.setText(word);
        holder.itemView.setTag(word);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public void onClick(View view) {
        Context context = inflater.getContext();

        Intent show = new Intent(context, MainActivity.class);
        show.putExtra(SearchManager.QUERY, (String)view.getTag());
        context.startActivity(show);
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView item;

        public ResultViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.textview_item_result);
        }
    }
}
