package com.yichiuan.moedict.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yichiuan.moedict.R
import com.yichiuan.moedict.common.TextUtil
import com.yichiuan.moedict.common.ui.StaticTextView
import moe.Definition
import moe.Heteronym
import java.util.*


class DefinitionAdapter(context: Context, private var heteronym: Heteronym
) : RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder>() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    private var sections = ArrayList<Section>()

    private var sectionSize: Int = 0

    class Section {
        var type: String? = null
        var definitions = ArrayList<Definition>()
        fun size(): Int {
            return if (type == null) definitions.size else definitions.size + 1
        }
    }

    init {
        initSections()
    }

    // Group definitions with the same type into section
    private fun initSections() {
        sections.clear()
        sectionSize = 0

        val definitionLength = heteronym.definitionsLength()

        var currentType: String? = ""
        var section: Section? = null

        for (i in 0 until definitionLength) {
            val definition = heteronym.definitions(i)
            val type = definition.type()

            if (i == 0 ||
                    currentType != null && currentType != type ||
                    currentType == null && type != null) {

                currentType = type

                section = Section()
                section.type = type
                sections.add(section)
                sectionSize++
            }
            section!!.definitions.add(definition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(inflater.inflate(R.layout.item_definition, parent, false))
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {

        val section = sections[position]

        if (section.type.isNullOrEmpty()) {
            holder.type.visibility = View.GONE
        } else {
            holder.type.visibility = View.VISIBLE
            holder.type.text = section.type
        }

        holder.type.text = section.type

        val builder = SpannableStringBuilder()

        var bulletStart = 0

        for (i in section.definitions.indices) {

            val definition = section.definitions[i]

            builder.append(TextUtil.obtainSpannedFrom(definition.defAsByteBuffer()))

            for (j in 0 until definition.examplesLength()) {
                if (j != 0 || builder.isNotEmpty()) {
                    builder.append("\n")
                }
                builder.append(TextUtil.obtainSpannedFrom(
                        definition.examples(j).u16strAsByteBuffer()))
            }

            for (j in 0 until definition.quotesLength()) {
                if (j != 0 || builder.isNotEmpty()) {
                    builder.append("\n")
                }
                builder.append(TextUtil.obtainSpannedFrom(
                        definition.quotes(j).u16strAsByteBuffer()))
            }

            for (j in 0 until definition.linksLength()) {
                if (j != 0 || builder.isNotEmpty()) {
                    builder.append("\n")
                }
                builder.append(TextUtil.obtainSpannedFrom(
                        definition.links(j).u16strAsByteBuffer()))
            }

            if (section.definitions.size > 1) {
                val bullet = BulletSpan()
                builder.setSpan(bullet, bulletStart, builder.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }

            if (i + 1 < section.definitions.size) {
                builder.append("\n")
                bulletStart = builder.length
            }
        }

        holder.definition.setText(builder)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class DefinitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var type: TextView = itemView.findViewById(R.id.textview_type)
        var definition: StaticTextView = itemView.findViewById(R.id.staticview_definition)
    }
}
