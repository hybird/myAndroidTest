package com.androidtest.t1;

import java.util.List;
import java.util.Locale;

import com.go2map.mapapi.G2MBusLineDetailResult;
import com.go2map.mapapi.G2MBusLineStopResult;
import com.go2map.mapapi.G2MGeocoderRequest;
import com.go2map.mapapi.G2MGeocoderResult;
import com.go2map.mapapi.G2MSearch;
import com.go2map.mapapi.G2MSearchListener;
import com.go2map.mapapi.G2MSearchResult;
import com.go2map.mapapi.LatLng;
import com.go2map.mapapi.MapView;
import com.go2map.mapapi.MapView.MapType;
import com.go2map.mapapi.Point;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LocationActivity extends Activity
{
	private LocationListener locationListener;

	EditText editText;

	MapView mapView;

	Button button1;
	// 全局变量定义
	private G2MSearch mSearch = new G2MSearch();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationapp);
		editText = (EditText) findViewById(R.id.editText1);
		mapView = (MapView) findViewById(R.id.sogouMap);
		button1 = (Button) findViewById(R.id.buttonMap);

		

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		

		 Location location =
		 locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		locationListener = new LocationListener()
		{
			public void onLocationChanged(Location location)
			{
				// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				// log it when the location changes
				if (location != null)
				{
					Log.i("SuperMap",
							"Location changed : 北纬: " + location.getLatitude()
									+ " 东经: " + location.getLongitude());

					editText.setText(editText.getText().append("\n")
							.append(" 北纬:" + location.getLatitude())
							.append("东经:" + location.getLongitude()));
					mapView.setCenter(new LatLng(location.getLatitude(), location.getLongitude()),10);
						
				}
			}

			public void onProviderDisabled(String provider)
			{
				// Provider被disable时触发此函数，比如GPS被关闭
			}

			public void onProviderEnabled(String provider)
			{
				// Provider被enable时触发此函数，比如GPS被打开
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras)
			{
				// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			}
		};
		// 从GPS获取位置信息,每隔1000ms更新一次
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, locationListener);

		button1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mSearch.init(new G2MSearchListener()
				{

					public void onGetSearchResultPOI(G2MSearchResult result)
					{
					}

					public void onGetSearchResultGeocode(
							G2MGeocoderResult result)
					{
						// 分析返回结果
						String status = result.getStatus();
						if (status.equals("ok"))
						{
							// 绘制匹配结果
							result.drawMarkers();
						} else
						{
							// 没有批评结果
						}
					}

					public void onGetSearchResultBusLineOrBusStop(
							G2MBusLineStopResult result)
					{
					}

					public void onGetSearchResultBusLineDetail(
							G2MBusLineDetailResult result)
					{
					}
				});
				// 提交地址匹配城市和地址请求
				G2MGeocoderRequest tmpGeocoderRequest = new G2MGeocoderRequest();

				tmpGeocoderRequest.setGeocoderRequest(editText.getText()
						.toString(), "北京");
				tmpGeocoderRequest.setMapView(mapView);
				mSearch.geocode(tmpGeocoderRequest);
			}
		});

		mapView.setCenter(new Point(12955992.1875,4827292.96875), 15);
		mapView.setDraggable(true);
		mapView.setMapType(MapType.SATELLITE);

	}

}
