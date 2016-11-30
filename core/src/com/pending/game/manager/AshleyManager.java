package com.pending.game.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.pending.game.EntityDao;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.systems.PhysicsSystem;
import com.pending.game.tools.MapperTools;

/**
 * 实体引擎
 * 
 * @author D
 * @date 2016年10月16日 下午8:22:07
 */
public class AshleyManager{
	
//	public static AshleyManager instance = new AshleyManager();
	
	/**
	 * ashley组件实体系统引擎
	 */
	public PooledEngine engine;
	
	/**
	 * 实体生产
	 */
	// TODO 不用多个 一个就行
	public EntityDao entityDao;
	
	private boolean isCopy = false;
	
	public AshleyManager() {
		engine = new PooledEngine();
		engine.addEntityListener(new AshleyManagerEntityListener());
		entityDao = new EntityDao();
	}
	
	/**
	 * 添加removeForCopy方法移出的Entity
	 * @param entity
	 */
	public void addCopy(Entity entity){
		isCopy = true;
		engine.addEntity(entity);
		
		isCopy = false;
	}
	
	/**
	 * 移出ECS, 但是不销毁。只有new的Entity有效
	 * @param entity
	 */
	public void removeForCopy(Entity entity){
		isCopy = true;
		engine.removeEntity(entity);
		
		isCopy = false;
	}
	
	/**
	 * 销毁
	 */
	public void disabled(){
		
		// 必须回收Entity，在销毁System
		engine.removeAllEntities();
		engine.clearPools();
		
		// 销毁物理引擎
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null)
			physicsSystem.physicsManager.dispose();
	}
	
	/**
	 * 组件监听
	 * 
	 * @author D
	 * @date 2016年11月14日
	 */
	private class AshleyManagerEntityListener implements EntityListener{
		
		@Override
		public void entityAdded(Entity entity) {
			
			if(isCopy)
				return;
			
			PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
			
			// 添加碰撞检测
			if(MapperTools.physicsCM.get(entity) != null){
				physicsSystem.physicsManager.addPhysicsRigidBody(entity);
			}
		}

		@Override
		public void entityRemoved(Entity entity) {
			
			if(isCopy)
				return;
			
			PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
			
			// 添加碰撞检测
			PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
			if(physicsComponent != null){
				physicsSystem.physicsManager.disposeBody(physicsComponent.rigidBody);
			}
			
		}
	}
	
}
