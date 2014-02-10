package com.servicios.fragments;

import com.servicios.infomovil.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentCirculacion extends Fragment {
	
	
	ImageView info;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.fragment_circula, container, false);
		
		info = (ImageView)v.findViewById(R.id.infoView);
		
		
		info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EngomadoActivity nuevofragment = new EngomadoActivity();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction =
		fragmentManager.beginTransaction().addToBackStack(null);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.replace(R.id.content_frame, nuevofragment);
				fragmentTransaction.commitAllowingStateLoss();
			}
		});
		return v;
		
	}

}
