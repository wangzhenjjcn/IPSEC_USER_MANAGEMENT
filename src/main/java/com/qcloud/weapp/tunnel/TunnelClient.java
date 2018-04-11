package com.qcloud.weapp.tunnel;

import org.myazure.configuration.PrimaryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import com.qcloud.weapp.Hash;

class TunnelClient {
	@Autowired
	private static PrimaryConfiguration primaryConfiguration;
	private static String _id = null;
	public static String getId()  {
		if (_id == null) {
			_id = Hash.md5(primaryConfiguration.getServerHost());
		}
		return _id;
	}
	
	public static String getKey()  {
		return primaryConfiguration.getTunnelSignatureKey();
	}
}
