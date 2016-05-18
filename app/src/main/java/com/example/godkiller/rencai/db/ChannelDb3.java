package com.example.godkiller.rencai.db;

import com.example.godkiller.rencai.base.Channel;

import java.util.ArrayList;
import java.util.List;


public class ChannelDb3 {
	
	private static List<Channel>   selectedChannel=new ArrayList<Channel>();
	
	static{
		selectedChannel.add(new Channel("1","待审核",0));
		selectedChannel.add(new Channel("2","已审核",0));

	}
	public static  List<Channel> getSelectedChannel(){
		 return selectedChannel;
	}
	
	
}
