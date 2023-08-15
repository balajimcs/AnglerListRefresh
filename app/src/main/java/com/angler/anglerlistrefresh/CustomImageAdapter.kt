    package com.angler.anglerlistrefresh

    import android.content.Context
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ArrayAdapter
    import android.widget.ImageView
    import com.bumptech.glide.Glide

    class CustomImageAdapter(context: Context, private val imageResourceIds: MutableList<Int>) :
        ArrayAdapter<Int>(context, R.layout.list_item_layout, imageResourceIds) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)
            val imageView = view.findViewById<ImageView>(R.id.itemImageView)

            // Load the image using the image resource ID
            val imageResourceId = imageResourceIds[position]
            Glide.with(context).load(imageResourceId).into(imageView)

            return view
        }

        fun addAll(newImages: List<Int>) {
            imageResourceIds.addAll(newImages)
            notifyDataSetChanged()
        }
    }
