package com.example.godkiller.rencai.db;

import com.example.godkiller.rencai.base.Channel;

import java.util.ArrayList;
import java.util.List;


public class ChannelDb {
	
	private static List<Channel>   selectedChannel=new ArrayList<Channel>();
	
	static{
		selectedChannel.add(new Channel("1","面试通知",0));
		selectedChannel.add(new Channel("2","录取通知",0));

	}
	public static  List<Channel> getSelectedChannel(){
		 return selectedChannel;
	}
	
	
}
