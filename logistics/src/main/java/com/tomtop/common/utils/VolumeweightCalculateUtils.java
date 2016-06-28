package com.tomtop.common.utils;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.google.common.collect.Maps;





public class VolumeweightCalculateUtils {

	/*public static void main(String[] args) {
		Map<String,Double> map=Maps.newHashMap();
		map.put("length", 50.0 );
		map.put("width",32.0 );
		map.put("high", 8.0 );
		map.put("volumeweight", 27.0 );
		getMutilProductVolumeweight(map,12.0,23.0,45.0,1.0);
		System.out.println(map);
	}*/
	
	
	public  static void getMutilIqtyVolumeweight(Map<String,Double> map,Double high,Double width,
			Double length,Double weight,Double volumeweight,int iqty){
		if(MapUtils.isEmpty(map)){
			map.put("length", 0.0 );
			map.put("width", 0.0 );
			map.put("weight", 0.0 );
			map.put("high", 0.0 );
			map.put("volumeweight", 0.0 );
		}
		for(int i=0;i<iqty;i++){
			getMutilProductVolumeweight(map, high, width, length, weight,volumeweight);
		}
	}
	public static void getMutilProductVolumeweight(Map<String,Double> map,Double high,Double width,Double length,Double weight,Double volumeweight){
		if(MapUtils.isEmpty(map)){
			map.put("length", 0.0 );
			map.put("width", 0.0 );
			map.put("weight", 0.0 );
			map.put("high", 0.0 );
			map.put("volumeweight", 0.0 );
		}
		double maxItem=0.0;
		Double itemHigh= high==null?0.0: high;
		Double itemWidth= width==null?0.0: width;
		Double itemLength= length==null?0.0: length;
		Double itemWeight= length==null?0.0: length;
		Double tempLength=map.get("length");//就
		Double tempWidth=map.get("width");
		Double tempHigh=map.get("high");
		Double totalLength=map.get("length")+itemLength;
		Double totalWidth=map.get("width")+itemWidth;
		Double totalHigh=map.get("high")+itemHigh;
		//挑选边最少的
		maxItem=totalLength;				
		maxItem=maxItem>totalWidth?totalWidth:maxItem;
		maxItem=maxItem>totalHigh?totalHigh:maxItem;
		Map<String,Boolean> falg=Maps.newHashMap();
		falg.put("replaced",false);
		
		//改变值
		map.put("length", getReplaceBord(falg,tempLength,itemLength,totalLength,maxItem));
		map.put("width", getReplaceBord(falg,tempWidth,itemWidth,totalWidth,maxItem));
		map.put("high", getReplaceBord(falg,tempHigh,itemHigh,totalHigh,maxItem));

		map.put("volumeweight",volumeweight==null?0.0: map.get("volumeweight")+volumeweight);
		map.put("weight",weight==null?0.0: map.get("weight")+itemWeight);
	}
	/**
	 * 
	 * @param oldBord		旧边
	 * @param addBord		增量边
	 * @param totalBord		总长度
	 * @param compareBord	被比较总长度
	 * @return
	 */
	private static Double getReplaceBord(Map<String,Boolean> falg,Double oldBord,Double addBord,Double totalBord,Double compareBord){
		if(!falg.get("replaced") && totalBord!=null && compareBord!=null && totalBord.compareTo(compareBord)==0){//替换总边
			falg.put("replaced",true);
			return oldBord+addBord;
		}else{//替换单独的边
			return addBord.compareTo(oldBord)==1?addBord:oldBord;
		}
	}
}
