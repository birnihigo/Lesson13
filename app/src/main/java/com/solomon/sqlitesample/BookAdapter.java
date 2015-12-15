package com.solomon.sqlitesample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter
{
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    private ArrayList<Book> mBooks;

    public BookAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mBooks = new ArrayList<Book>();
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.content_main, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.book_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.book_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.book_author);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        Book book = (Book) getItem(position);

        // See if there is a cover ID in the Object
        if (book.getImageId().contains("cover_i")) {

            // If so, grab the Cover ID out from the object
            String imageID = book.getImageId();

            // Construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.book_cover).into(holder.thumbnailImageView);
        } else {

            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.book_cover);
        }


// Send these Strings to the TextViews for display
        holder.titleTextView.setText(book.getBookTitle());
        holder.authorTextView.setText(book.getBookAuthor());

        return convertView;
    }

    public void updateData(ArrayList<Book> books) {
        // update the adapter's dataset
        mBooks = books;
        notifyDataSetChanged();
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}