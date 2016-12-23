package com.pending.game;

public class GameConfig {

	/**
	 * 游戏设计分辨率
	 */
	public final static float width = 540;
	public final static float height = 960;
	
	public static float cameraOffset = 0;
	
	/**
	 * UI边距，不同手机屏幕比例不同，没有边距会显示不全
	 */
	public final static byte UIpad = 35;
	
	/**
	 * 地图图块大小
	 */
	public final static byte tileSize = 32; 
	
	/**
	 * 角色占用大小，4个图块
	 */
	public final static byte characterTileSize = 64; 
	
	
	/**
	 * 游戏速度
	 */
	public static float gameSpeed = 1;
	
	/**
	 * UI的debug模式
	 */
	public final static boolean UIdebug = false;
	
	/**
	 * 物理引擎的debug模式
	 */
	public final static boolean physicsdebug = true;
	
	/**
	 * fps
	 */
	public final static boolean fpsDebug = false;
}
