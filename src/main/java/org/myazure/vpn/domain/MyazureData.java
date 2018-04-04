package org.myazure.vpn.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "data")
public class MyazureData {
	@Id
	@Column(name = "mkey", nullable = false, length = 250)
	private String mkey;

	@Column(name = "mvalue", nullable = true, length = 4000)
	private String mvalue;

	public MyazureData() {

	}

	public MyazureData(String key, String value) {
		this.mkey = key;
		this.mvalue = value;
	}

	public String getMkey() {
		return mkey;
	}

	public void setMkey(String mkey) {
		this.mkey = mkey;
	}

	public String getMvalue() {
		return mvalue;
	}

	public void setMvalue(String mvalue) {
		this.mvalue = mvalue;
	}

}
