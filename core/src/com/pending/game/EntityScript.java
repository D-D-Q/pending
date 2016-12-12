package com.pending.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * 实体脚本的基类
 * 
 * @author D
 * @date 2016年10月14日
 */
public class EntityScript  {

	/**
	 * 该脚本属于的实体
	 */
	public Entity entity;
	
	/**
	 * CollisionComponent碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 * @return true 继续执行默认组件的操作
	 */
	public boolean beginContact(Contact contact, Entity target){
		return true;
	}
	
	/**
	 * CollisionComponent结束碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 * @return true 继续执行默认组件的操作
	 */
	public boolean endContact(Contact contact, Entity target){
		return true;
	}
	
	/**
	 * 新碰撞点, 已碰撞之后的移动会触发
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 */
	public void preSolve(Contact contact, Manifold oldManifold, Entity target) {
	}

	/**
	 * 碰撞点产生力, 已碰撞之后的移动会触发
	 */
	public void postSolve(Contact contact, ContactImpulse impulse, Entity target) {
	}
	
	/**
	 * 每帧调用
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime){}
	
}
