package com.example.locobee;

import android.content.Context;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements Filterable {

    private Context mContext;
    private List<Upload> mUploads;
    private List<Upload> mUploadsFull;
    private OnItemClickListener mListener;

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener
    {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public TextView textViewPrice;
        public TextView textViewQuantity;
        public ImageView imageView;
        public ImageView mDeleteImage;

        public ImageViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            mDeleteImage = itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onEditItemClick(position);
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mListener.onEditItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem editItem = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem addToCart = menu.add(Menu.NONE, 2, 2, "Add to Cart");
            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete");

            editItem.setOnMenuItemClickListener(this);
            addToCart.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId())
                    {
                        case 1:
                            mListener.onEditItemClick(position);
                            return true;
                        case 2:
                            mListener.onAddToCartClick(position);
                            return true;
                        case 3:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
        mUploadsFull = new ArrayList<>(uploads);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v, mListener);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);

        holder.textViewTitle.setText(uploadCurrent.getmTitle());
        holder.textViewDescription.setText(uploadCurrent.getmDescription());
        holder.textViewQuantity.setText(uploadCurrent.getmQuantity());
        holder.textViewPrice.setText(uploadCurrent.getmPrice());

        Picasso.get()
                .load(uploadCurrent.getmImageUrl())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return mUploads.size();
    }

    @Override
    public Filter getFilter() {
        return imageFilter;
    }

    private Filter imageFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            List<Upload> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(mUploadsFull);
            }
            else
            {
                String filteredPattern = constraint.toString().toLowerCase().trim();

                for(Upload upload : mUploadsFull)
                {
                    if(upload.getmTitle().toLowerCase().contains(filteredPattern))
                    {
                        filteredList.add(upload);
                    }
                    else if(upload.getmDescription().toLowerCase().contains(filteredPattern))
                    {
                        filteredList.add(upload);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUploads.clear();
            mUploads.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener
    {
        void onEditItemClick(int position);
        void onAddToCartClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

//    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
//            MenuItem.OnMenuItemClickListener
//
//    {
//        public TextView textViewTitle;
//        public TextView textViewDescription;
//        public TextView textViewPrice;
//        public TextView textViewQuantity;
//        public ImageView imageView;
//        public ImageView mDeleteImage;
//
//        public ImageViewHolder(View itemView, final OnItemClickListener listener)
//        {
//            super(itemView);
//
//            imageView = itemView.findViewById(R.id.imageView);
//            textViewTitle = itemView.findViewById(R.id.text_view_title);
//            textViewDescription = itemView.findViewById(R.id.text_view_description);
//            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
//            textViewPrice = itemView.findViewById(R.id.text_view_price);
//
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    if(listener != null)
//                    {
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION)
//                        {
//                            listener.onEditItemClick(position);
//                        }
//                    }
//                }
//            });
//
//            mDeleteImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null)
//                    {
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION)
//                        {
//                            listener.onDeleteClick(position);
//                        }
//                    }
//                }
//            });
//
//            itemView.setOnCreateContextMenuListener(this);
//
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
//
//            MenuItem editItem = menu.add(Menu.NONE, 1, 1, "Edit Item");
//            MenuItem addItemToCart = menu.add(Menu.NONE, 2, 2, "Add to Cart");
//            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete Item");
//
//            editItem.setOnMenuItemClickListener(this);
//            addItemToCart.setOnMenuItemClickListener(this);
//            delete.setOnMenuItemClickListener(this);
//        }
//
//            public boolean onMenuItemClick(MenuItem item)
//            {
//                if(listener != null)
//                {
//                    int position = getAdapterPosition();
//                    if(position != RecyclerView.NO_POSITION)
//                    {
//                        switch (item.getItemId())
//                        {
//                            case 1:
//                                listener.onEditItemClick(position);
//                                return true;
//
//                            case 2:
//                                listener.onAddToCartClick(position);
//                                return true;
//
//                            case 3:
//                                listener.onDeleteClick(position);
//                                return true;
//                        }
//                    }
//                }
//                return false;
//            }
//
//            public void onClick(View v)
//            {
//
//            }
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            return false;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if( != null)
//            {
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION)
//                {
//                    listener.onAddToCartClick(position);
//                }
//            }
//        }
//
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//        }
//    }



//    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
//            MenuItem.OnMenuItemClickListener
//    {
//        public TextView textViewTitle, textViewDescription, textViewPrice, textViewQuantity;
//        public ImageView imageView, mDeleteImage;
//
//        public ImageViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
//            super(itemView);
//
//            textViewTitle = itemView.findViewById(R.id.text_view_title);
//            textViewDescription = itemView.findViewById(R.id.text_view_description);
//            textViewPrice = itemView.findViewById(R.id.text_view_price);
//            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
//            imageView = itemView.findViewById(R.id.imageView);
//
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//
//        }
//
//
////        public void decreaseQuantity()
////        {
////            if(Integer.parseInt(quantity.getText().toString()) > 0)
////                {
////                    quantity.setText(Integer.parseInt(quantity.getText().toString()) - 1);
////                }
////                else if(Integer.parseInt(quantity.getText().toString()) == 0)
////                {
////                    quantity.setText("Out");
////                }
////        }
//
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
//
//            MenuItem editItem = menu.add(Menu.NONE, 1, 1, "Edit Item");
//            MenuItem addItemToCart = menu.add(Menu.NONE, 2, 2, "Add to Cart");
//            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete Item");
//
//            editItem.setOnMenuItemClickListener(this);
//            addItemToCart.setOnMenuItemClickListener(this);
//            delete.setOnMenuItemClickListener(this);
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            if(mListener != null)
//            {
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION)
//                {
//                    switch (item.getItemId())
//                    {
//                        case 1:
//                            mListener.onEditItemClick(position);
//                            return true;
//
//                        case 2:
//                            mListener.onAddToCartClick(position);
//                            return true;
//
//                        case 3:
//                            mListener.onDeleteClick(position);
//                            return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if(listener != null)
//            {
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION)
//                {
//                    listener.onAddToCartClick(position);
//                }
//            }
//        }
//    }





}
