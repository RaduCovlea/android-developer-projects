package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        News currentNew = getItem(position);
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentNew.getTitle());
        TextView dateAndTime = (TextView) listItemView.findViewById(R.id.dateandtime);
        dateAndTime.setText(currentNew.getDateAndTime());
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentNew.getSection());
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        ArrayList<String> authorsArray = currentNew.getAuthorArrayList();
        if (authorsArray == null) {
            author.setText(getContext().getString(R.string.no_author_found));
        } else {
            StringBuilder authorString = new StringBuilder();
            for (int i = 0; i < authorsArray.size(); i++) {
                authorString.append(authorsArray.get(i));
                if ((i + 1) < authorsArray.size()) {
                    authorString.append(", ");
                }
            }
            ;
            author.setText(authorString.toString());
            author.setVisibility(View.VISIBLE);

        }
        return listItemView;
    }
}
