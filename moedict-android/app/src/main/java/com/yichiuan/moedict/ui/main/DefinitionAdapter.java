package com.yichiuan.moedict.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.common.ui.StaticTextView;
import com.yichiuan.moedict.common.TextUtil;

import java.util.ArrayList;

import moe.Definition;
import moe.Heteronym;


public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {

    static class Section {
        public String type;
        public ArrayList<Definition> definitions = new ArrayList<>();
        public int size() {
            return type == null ? definitions.size() : definitions.size() + 1;
        }
    }

    LayoutInflater inflater;
    Heteronym heteronym;

    ArrayList<Section> sections = new ArrayList<>();
    int sectionSize;

    public DefinitionAdapter(Context context, Heteronym heteronym) {
        inflater = LayoutInflater.from(context);
        this.heteronym = heteronym;
        initSections();
    }

    // Group definitions with the same type into section
    private void initSections() {
        sections.clear();
        sectionSize = 0;

        int definitionLength = heteronym.definitionsLength();

        String currentType = "";
        Section section = null;

        for (int i = 0; i < definitionLength; ++i) {
            Definition definition = heteronym.definitions(i);
            String type = definition.type();

            if (i == 0 ||
                currentType != null && !currentType.equals(type) ||
                currentType == null && type != null) {

                currentType = type;

                section = new Section();
                section.type = type;
                sections.add(section);
                sectionSize++;
            }
            section.definitions.add(definition);
        }
    }

    @Override
    public DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefinitionViewHolder(inflater.inflate(R.layout.item_definition, parent, false));
    }

    @Override
    public void onBindViewHolder(DefinitionViewHolder holder, int position) {

        Section section = sections.get(position);

        if (section.type == null || section.type.isEmpty()) {
            holder.type.setVisibility(View.GONE);
        } else {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText(section.type);
        }

        holder.type.setText(section.type);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        int bulletStart = 0;

        for (int i = 0; i < section.definitions.size(); ++i) {

            Definition definition = section.definitions.get(i);

            builder.append(TextUtil.INSTANCE.obtainSpannedFrom(definition.defAsByteBuffer()));

            for (int j = 0; j < definition.examplesLength(); ++j) {
                if (j != 0 || builder.length() != 0) {
                    builder.append("\n");
                }
                builder.append(TextUtil.INSTANCE.obtainSpannedFrom(
                        definition.examples(j).u16strAsByteBuffer()));
            }

            for (int j = 0; j < definition.quotesLength(); ++j) {
                if (j != 0 || builder.length() != 0) {
                    builder.append("\n");
                }
                builder.append(TextUtil.INSTANCE.obtainSpannedFrom(
                        definition.quotes(j).u16strAsByteBuffer()));
            }

            for (int j = 0; j < definition.linksLength(); ++j) {
                if (j != 0 || builder.length() != 0) {
                    builder.append("\n");
                }
                builder.append(TextUtil.INSTANCE.obtainSpannedFrom(
                        definition.links(j).u16strAsByteBuffer()));
            }

            if (section.definitions.size() > 1) {
                BulletSpan bullet = new BulletSpan();
                builder.setSpan(bullet, bulletStart, builder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            if (i + 1 < section.definitions.size()) {
                builder.append("\n");
                bulletStart = builder.length();
            }
        }

        holder.definition.setText(builder);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class DefinitionViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        StaticTextView definition;

        public DefinitionViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.textview_type);
            definition = itemView.findViewById(R.id.staticview_definition);
        }
    }

}
