package com.example.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Country_names_popup extends AppCompatDialogFragment
{
    SearchView userssearch;
    ListView listview;
    private ArrayAdapter<String> listAdapter,codeAdapter;
    String codes;
    CountrynamesListener listener;
    int aPosition;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder alertdialog=new AlertDialog.Builder(getActivity());
        AlertDialog dialog;

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.countryname_dialog,null);

        userssearch=view.findViewById(R.id.userssearch);
        listview=view.findViewById(R.id.listview);


        String[] listViewAdapterContent = {"#School", "#House", "#Building", "#Food", "#Sports", "#Dress", "#Ring"};

        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1,CountryData.countryNames);
        listview.setAdapter(listAdapter);

        codeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1,CountryData.countryAreaCodes);

        alertdialog.setView(view);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s=listAdapter.getItem(position);

                for(int i=0; i < CountryData.countryNames.length; i++)
                    if(CountryData.countryNames[i].contains(s))
                        aPosition = i;

                codes=codeAdapter.getItem(aPosition);
                listener.applycodes(codes);
                dismiss();
            }
        });


        userssearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                               @Override
                                               public boolean onQueryTextSubmit(String query) {
                                                   return false;
                                               }

                                               @Override
                                               public boolean onQueryTextChange(String newText)
                                               {

                                                   listAdapter.getFilter().filter(newText);
                                                   return false;
                                               }
                                           });




        return  alertdialog.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(CountrynamesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement listener");
        }
    }

    public interface CountrynamesListener
    {
        void applycodes(String codes);
    }

}
