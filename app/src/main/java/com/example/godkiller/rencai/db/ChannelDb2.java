package com.example.godkiller.rencai.db;

import com.example.godkiller.rencai.base.Channel;

import java.util.ArrayList;
import java.util.List;


public class ChannelDb2 {
	
	private static List<Channel>   selectedChannel=new ArrayList<Channel>();
	
	static{
		selectedChannel.add(new Channel("3","简历名单",0));
		selectedChannel.add(new Channel("4","面试名单",0));
		selectedChannel.add(new Channel("5","录取名单",0));

	}
	public static  List<Channel> getSelectedChannel(){
		 return selectedChannel;
	}
	
	
}
