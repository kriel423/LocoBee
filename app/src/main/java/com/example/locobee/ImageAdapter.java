package com.example.locobee;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;

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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener
    {
        public TextView textViewTitle;
        public TextView textViewCategory;
        public TextView textViewPrice;
        public TextView textViewQuantity;
        public ImageView imageView;
        public Button decreaseQuantity;
        public String itemCountValue;

        public ImageViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_display);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);

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


            decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemCountValue = textViewQuantity.getText().toString();

                    if(mListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            if(itemCountValue.equals("0"))
                            {
                                textViewQuantity.setText("Sold Out");
                                textViewQuantity.setTextColor(Color.RED);
                            }
                            else if(Integer.parseInt(itemCountValue) > 0)
                            {
                                String val = String.valueOf(Integer.parseInt(itemCountValue)-1);
                                textViewQuantity.setText(val);
                            }
                            else if(itemCountValue.equals("Sold Out"))
                            {
                                Toast.makeText(mContext, "Item already sold out", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(mContext, "Item already sold out", Toast.LENGTH_SHORT).show();
                            }
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
        holder.textViewCategory.setText(uploadCurrent.getmCategory());
        holder.textViewQuantity.setText(uploadCurrent.getmQuantity());
        holder.textViewPrice.setText(uploadCurrent.getmPrice());
        Picasso.get()
                .load(uploadCurrent.getmImageUrl())
                .placeholder(R.drawable.ic_image)
                .fit()
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
                    else if(upload.getmCategory().toLowerCase().contains(filteredPattern))
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



}
