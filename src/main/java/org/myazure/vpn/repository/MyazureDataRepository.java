package org.myazure.vpn.repository;

import java.util.List;

import org.myazure.vpn.domain.MyazureData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyazureDataRepository extends
		PagingAndSortingRepository<MyazureData, String> {
	public MyazureData findOneByMkey(String key);
	public List<MyazureData>  findByMkeyStartingWith(String key);
	
}