package com.servicios.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.servicios.infomovil.R;

public class FragmentInicio extends Fragment {
	//Fragmento1
		TextView txttemp; //temperatura
		TextView txtcond; //condiciones climatologicas
		TextView txtfecha; //fecha 
		ImageView imgClima;//imagen clima
		RelativeLayout lyClima; //layout instanciado para cambiarle el fondo
		
		
		//Fragemento2
		TextView txtDia;
		TextView txtColor;
		String diaSemana="";
		Calendar cal;
		
		//Fragmento3
		TextView txtEstado;
		
		//Temperatura,Condicion y Calidad Aire
		AsyncHttpClient cliente ;
		AsyncHttpClient estadoAire;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState)
		{
			
			View v = inflater.inflate(R.layout.fragment_inicio,container,false);
			
			
			//Fragmento 1
			txtcond = (TextView)v.findViewById(R.id.clima);
			txttemp = (TextView)v.findViewById(R.id.temperatura);
			txtfecha= (TextView)v.findViewById(R.id.fecha);
			imgClima=(ImageView)v.findViewById(R.id.iconoClima);
			lyClima=(RelativeLayout)v.findViewById(R.id.lytfgtClima);

			
		//Fragmento 2
			txtDia=(TextView)v.findViewById(R.id.dia);
			txtColor=(TextView)v.findViewById(R.id.colorCircula);
			
			//Fragmento 3
			txtEstado=(TextView)v.findViewById(R.id.estado);
			
			
			//cliente nos da Temperatura y Condicion
			cliente = new AsyncHttpClient();

			//estadoAire nos devuelve la calidad del aire
			estadoAire=new AsyncHttpClient();
			
			cliente.get("http://datos.labplc.mx/aire/clima.json", new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response) {
			// TODO Auto-generated method stub
			Log.e("Dime que tiene", "el entero: "+ response);
			
			
			try{
				JSONObject json = new JSONObject(response);
			
				JSONObject consulta = json.getJSONObject("consulta");
				
				JSONObject clima = consulta.getJSONObject("clima");
				Log.e("clima", clima.toString());
				
				String condicion = clima.getString("condicion");
				String tempratura = clima.getString("temperatura");
				
				String parametrosClima[]= condicion.split("_");
				for(int i=0;i<parametrosClima.length;i++)
				{
					String mayuscula="";
					StringBuffer condicionclimatica= new StringBuffer(parametrosClima[i]);
					mayuscula=condicionclimatica.charAt(0)+"";
					mayuscula=mayuscula.toUpperCase();
					parametrosClima[i]=parametrosClima[i].replaceFirst(condicionclimatica.charAt(0)+"",mayuscula.toUpperCase());
					txtcond.append(parametrosClima[i]+" ");
				}
				
				
				txttemp.setText(tempratura + "¼C");
				colorearTemperatura(Integer.parseInt(tempratura));
				conocerClima(parametrosClima);
				
				
			}catch(JSONException e){
				e.printStackTrace();
				
				//Toast.makeText(getApplicationContext(), "No hay internet", Toast.LENGTH_LONG).show();
				
			}
			
			}
			
			
			});
			
			//inicio metodo onSucces del objeto estadoAire
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
			
			txtfecha.setText(getFecha());
			txtDia.setText(getDiaSemana());
			
			return v;
		}
		public void conocerClima(String clima[])
		{
			if(clima[0].equalsIgnoreCase("despejado"))
			{
				imgClima.setImageResource(R.drawable.nublado);
				
			}
			else if(clima[0].equalsIgnoreCase("lluvioso"))
			{
				imgClima.setImageResource(R.drawable.lluvioso);
			}
		}
		public String getFecha()
		{
			Calendar cal= new GregorianCalendar();
			SimpleDateFormat df= new SimpleDateFormat("dd-MM-yyyy");
			String fecha=df.format(cal.getTime());
			return fecha;
		}
		public String getDiaSemana()
		{
			Date hoy= new Date();
			cal=Calendar.getInstance();
			cal.setTime(hoy);
			int noDia=cal.get(Calendar.DAY_OF_WEEK);
			switch(noDia)
			{
			case 2:
				diaSemana="Lunes";
				txtColor.setBackgroundResource(R.drawable.amarillo);
				txtColor.setText("     5, 6");
				break;
			case 3:
				diaSemana="Martes";
				txtColor.setBackgroundResource(R.drawable.rosa);
				txtColor.setText("     7, 8");
				break;
			case 4:
				diaSemana="Miercoles";
				txtColor.setBackgroundResource(R.drawable.rojo);
				txtColor.setText("     3, 4");
				break;
			case 5:
				diaSemana="Jueves";
				txtColor.setBackgroundResource(R.drawable.verde);
				txtColor.setText("     1, 2");
				break;
			case 6:
				diaSemana="Viernes";
				txtColor.setBackgroundResource(R.drawable.azul);
				txtColor.setText("     9, 0");
				break;
			case 7:
				diaSemana="Sabado";
				if(cal.DAY_OF_MONTH<=7)
				{
					txtColor.setBackgroundResource(R.drawable.amarillo);
					txtColor.setText("     5, 6");
				}
				else if(cal.DAY_OF_MONTH<=14)
				{
					txtColor.setBackgroundResource(R.drawable.rosa);
					txtColor.setText("     7, 8");
				}
				else if(cal.DAY_OF_MONTH<=21)
				{
					txtColor.setBackgroundResource(R.drawable.rojo);
					txtColor.setText("     3, 4");
				}
				else if(cal.DAY_OF_MONTH==29||cal.DAY_OF_MONTH==30||cal.DAY_OF_MONTH==31)
				{
					txtColor.setBackgroundResource(R.drawable.azul);
					txtColor.setText("     9, 0");
				}
				else 
				{
					txtColor.setBackgroundResource(R.drawable.verde);
					txtColor.setText("     1, 2");
				}
				break;
				default:
					diaSemana="Domingo";
					break;
					
			}
			return diaSemana;
		}
		public void colorearTemperatura(int temperatura)
		{
			if(temperatura<=0||temperatura<=10)
			{
				lyClima.setBackgroundResource(R.drawable.frio);
			}
			else if(temperatura<=20)
			{
				lyClima.setBackgroundResource(R.drawable.templado);
			}
			else if(temperatura<=35)
			{
				lyClima.setBackgroundResource(R.drawable.calido);
			}
			else
			{
				lyClima.setBackgroundResource(R.drawable.defecto);
			}
		}

		
}
