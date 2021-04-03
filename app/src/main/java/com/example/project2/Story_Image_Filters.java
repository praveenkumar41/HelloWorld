package com.example.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2.Adapter.ThumbnailAdapter;
import com.example.project2.Interface.FilterListFragmentListener;
import com.example.project2.Utils.BitmapUtils;
import com.example.project2.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;


public class Story_Image_Filters extends Fragment //implements FilterListFragmentListener
{

//    RecyclerView recyclerView;
//    private FilterListFragmentListener listener;
//    ThumbnailAdapter adapter;
//    List<ThumbnailItem> thumbnailItems;
//    Bitmap imagebitmap;
//    String temp1;

//    public void setListener(FilterListFragmentListener listener) {
  //      this.listener = listener;
   // }

    public Story_Image_Filters() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View itemview= inflater.inflate(R.layout.fragment_story__image__filters, container, false);
        thumbnailItems=new ArrayList<>();
        adapter=new ThumbnailAdapter(thumbnailItems,this,getActivity());

//        Bitmap ss=getArguments().getParcelable("stringbitmap");

        recyclerView=itemview.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0,getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(null);
        return itemview;
    }

    private void displayThumbnail(final Bitmap bitmap)
    {
        final Chatfragment chatfragment=new Chatfragment();

        final Runnable r=new Runnable() {
            @Override
            public void run() {
                Bitmap thumbimg;
                if(bitmap==null){
                    Bitmap ss=Chatfragment.bundle.getParcelable("bitmap");
                    thumbimg=ss;//(Bitmap) BitmapUtils.getbitmapFromStoryimageview(getActivity(),imagebitmap,100,100);
                }
                else {
                    thumbimg=Bitmap.createScaledBitmap(bitmap,100,100,false);
                }
                if(thumbimg==null){
                    return;
                }
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                ThumbnailItem thumbnailItem=new ThumbnailItem();
            //    thumbnailItem.image=thumbimg;
                Bitmap ss1=Chatfragment.bundle.getParcelable("bitmap");
                thumbnailItem.image=ss1;
                thumbnailItem.filterName="Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter>filters= FilterPack.getFilterPack(getActivity());

                for(Filter filter:filters){
                    ThumbnailItem t1=new ThumbnailItem();
                    //t1.image=thumbimg;
                    t1.image=ss1;
                    t1.filter=filter;
                    t1.filterName=filter.getName();
                    ThumbnailsManager.addThumb(t1);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onfilterselected(Filter filter) {
        if(listener!=null){
            listener.onfilterselected(filter);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
*/