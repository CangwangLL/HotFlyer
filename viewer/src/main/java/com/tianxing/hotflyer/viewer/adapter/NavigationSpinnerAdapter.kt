package com.tianxing.hotflyer.viewer.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import java.util.Arrays

import com.tianxing.hotflyer.viewer.R

/**
 * Project: JAViewer
 */

class NavigationSpinnerAdapter<T> @JvmOverloads constructor(context: Context, @LayoutRes resource: Int, @IdRes textViewResourceId: Int = 0, objects: List<T> = ArrayList()) : ArrayAdapter<T>(context, resource, textViewResourceId, objects) {

    private val mLayoutInflater: LayoutInflater

    constructor(context: Context, @LayoutRes resource: Int, objects: Array<T>) : this(context, resource, 0, Arrays.asList<T>(*objects)) {}

    constructor(context: Context, @LayoutRes resource: Int, @IdRes textViewResourceId: Int, objects: Array<T>) : this(context, resource, textViewResourceId, Arrays.asList<T>(*objects)) {}

    constructor(context: Context, @LayoutRes resource: Int, objects: List<T>) : this(context, resource, 0, objects) {}

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        if (view == null || view.tag.toString() != "DROPDOWN") {
            view = mLayoutInflater.inflate(R.layout.view_drop_down, parent, false)
            view!!.tag = "DROPDOWN"
        }

        val textView = view.findViewById<View>(R.id.dropdown_text) as TextView
        textView.text = getItem(position)!!.toString()

        return view
    }
}
