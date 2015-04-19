package com.zx.POJO;

import java.util.Random;

public class GameData {
	public int totalTimes = 0;
	public int sucTimes = 0;
	public int curTimes = 0;
	
	public int sucIndex=0;
	public int userIndex=0;
	public int openIndex=0;
	
	public Random random=new Random(System.currentTimeMillis());
	
	public String[] list=new String[3];
}
