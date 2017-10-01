package com.example.android.haqdarshak_akshay_android_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Akshay on 01-10-2017.
 * this is a custom adaper for the recyler view
 */

 public class customAdapter extends RecyclerView.Adapter<customAdapter.customViewHolder> {
        private ArrayList<ArrayList<String>> feedList;

    //constructor
        public customAdapter(ArrayList<ArrayList<String>> feeds){
            feedList = feeds;
        }

        @Override
        public customAdapter.customViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate view and attach to parent
            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;
            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            customViewHolder viewHolder = new customViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(customAdapter.customViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return feedList.size();
        }

        public class customViewHolder extends RecyclerView.ViewHolder{
            TextView feedHolder, dateHolder;
            public customViewHolder(View itemView) {
                super(itemView);
                feedHolder = (TextView) itemView.findViewById(R.id.feedHolder);
                dateHolder = (TextView) itemView.findViewById(R.id.dateHolder);
            }
            //helper method for binding data
            void bind(int listIndex) {
                feedHolder.setText(feedList.get(listIndex).get(0));
                dateHolder.setText(feedList.get(listIndex).get(1));
            }
        }
}
