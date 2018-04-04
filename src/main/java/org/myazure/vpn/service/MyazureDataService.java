package org.myazure.vpn.service;

import java.util.List;

import org.myazure.vpn.domain.MyazureData;


public interface MyazureDataService {
	public String getValue(String key);
	public MyazureData getMyazureData(String key);
	public MyazureData save(MyazureData myazureData);
	public MyazureData save(String key,String value);
	public MyazureData save(String key,Integer value);
	public List<MyazureData> findMyazureDataByMkeyStartingWith(String keyPreString);
	public List<MyazureData> deleteAllMyazureDatasByKeyStartWith(String keyPreString);
	public String getIDValue(String iDkey,String key);
	public String getIDValue(String iDkey,Integer key);
	public String getKeyPreValue(String preKey,String key);
}
