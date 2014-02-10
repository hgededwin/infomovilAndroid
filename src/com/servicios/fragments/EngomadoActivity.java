package com.servicios.fragments;

import com.servicios.infomovil.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EngomadoActivity extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSta){
		
		View v = inflater.inflate(R.layout.fragment_info_activity, container, false);
		
		return v;
		
	}

}
