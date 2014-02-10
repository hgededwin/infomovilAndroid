package com.servicios.infomovil;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.servicios.fragments.FragmentAcerca;
import com.servicios.fragments.FragmentAutos;
import com.servicios.fragments.FragmentCirculacion;
import com.servicios.fragments.FragmentDepositos;
import com.servicios.fragments.FragmentInfracciones;
import com.servicios.fragments.FragmentInicio;
import com.servicios.fragments.FragmentTenencia;
import com.servicios.fragments.FragmentVerificacion;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends SherlockFragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mFragmentTitles;
	TextView txtEstado;
	AsyncHttpClient estadoAire;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
		mFragmentTitles = getResources().getStringArray(R.array.MenuFragments);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		mDrawerList = (ListView)findViewById(R.id.drawer_list);
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mFragmentTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, 
				mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
			public void onDrawerClosed(View v){
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}
			public void onDrawerOpened(View v){
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null){
			selectItem(0);
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)){
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;
		case R.id.action_settings_dos:
			
			actualizar();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void actualizar() {
		
		estadoAire.get("http://datos.labplc.mx/aire.json",new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response)
			{
				try
				{
					JSONObject atmosfera = new JSONObject(response);
					JSONObject consultarAire = atmosfera.getJSONObject("consulta");
					JSONObject calidadAire = consultarAire.getJSONObject("calidad");
					
					String calidad = calidadAire.getString("categoria");
					
					String mayuscula=calidad.charAt(0)+"";
					mayuscula.toUpperCase();
					calidad=calidad.replaceFirst(calidad.charAt(0)+"", mayuscula.toUpperCase());
					txtEstado.setText(calidad);
				}
				catch(Exception e)
				{
					Log.e("Ha surgido un problema", "Verifica-----> " + e);
				}
			}
		});
		
		
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id){
			selectItem(position);
		}
	}
	
	private void selectItem(int position){
		Fragment newFragment = new FragmentInicio();
		FragmentManager fm = getSupportFragmentManager();
		switch(position){
		case 0:
			newFragment = new FragmentInicio();
			break;
		case 1:
			newFragment = new FragmentAutos();
			break;
		case 2:
			newFragment = new FragmentTenencia();
			break;
		case 3:
			newFragment = new FragmentVerificacion();
			break;
		case 4:
			newFragment = new FragmentCirculacion();
			break;
		case 5:
			newFragment = new FragmentInfracciones();
			break;
		case 6:
			newFragment = new FragmentDepositos();
			break;
		case 7:
			newFragment = new FragmentAcerca();
			break;
		}
		fm.beginTransaction()
		.replace(R.id.content_frame, newFragment)
		.commit();
		
		mDrawerList.setItemChecked(position, true);
		setTitle(mFragmentTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	@Override
	public void setTitle(CharSequence title){
		mTitle = title;
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setIcon(android.R.color.transparent);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}



}
