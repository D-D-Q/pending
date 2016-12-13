package com.pending.game.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.pending.game.GameConfig;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.tools.MapperTools;

/**
 * box2d物理引擎管理器
 * 非线程安全
 * 
 * @author D
 * @date 2016年10月12日
 */
public class PhysicsManager {
	
//	public static PhysicsManager instance = new PhysicsManager();
	
	/**
	 * 物理世界频率, 每秒
	 */
	public static final float TIME_STEP = 300f;
	
	/**
	 * 物理世界速度和位置的计算。数值越大，效果越细腻，计算量也就越大，最高不要超过10 
	 */
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	/**
	 * box2d的物理世界
	 */
	public World world;
	
	/**
	 * 物理引擎debug绘制对象
	 */
	private Box2DDebugRenderer debugRenderer;
	
	public PhysicsManager() {
		world = new World(new Vector2(0, -1200), true);  // 参数：无重力, 休眠;
		if(GameConfig.physicsdebug)
			debugRenderer = new Box2DDebugRenderer();
	}
	
	/**
	 * 物理引擎debug绘制
	 */
	public void debugRender(Camera camera){
		if(GameConfig.physicsdebug){
	    	debugRenderer.render(world, camera.combined);
	    }
	}
	
	/**
	 * 添加刚体
	 * categoryBits是默认值0x0001
	 * 
	 * @param entity
	 * @return
	 */
	public void addPhysicsRigidBody(Entity entity){
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		Body body = create(physicsComponent.bodyType, physicsComponent.shape,
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		body.setUserData(entity); // 刚体携带实体
		
		physicsComponent.rigidBody = body;
		
		physicsComponent.shape.dispose();
	}
	
	/**
	 * 添加传感器
	 * categoryBits是默认值0x0001
	 * 
	 * @param entity
	 * @return
	 */
	public void addSensorRigidBody(Entity entity){
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		Body body = createSensor(physicsComponent.bodyType, physicsComponent.shape,
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		body.setUserData(entity); // 刚体携带实体
		
		physicsComponent.rigidBody = body;
		
		physicsComponent.shape.dispose();
	}
	
	/**
	 * 生成正常刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param position 生成位置
	 * @return
	 */
	private Body create(BodyType bodyType, Shape shape, Vector2 position){
		return create(bodyType, shape, false, (short)0, (short)1, (short)-1, position);
	}
	
	/**
	 * 生成正常刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param groupIndex isSensor是false时生效。 同组的正数的碰撞,负数不碰撞。0或不同组正常碰撞
	 * @param position 生成位置
	 * @return
	 */
	private Body create(BodyType bodyType, Shape shape, short groupIndex, Vector2 position){
		return create(bodyType, shape, false, groupIndex, (short)1, (short)-1, position);
	}
	
	/**
	 * 生成正常刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param groupIndex isSensor是false时生效。 同组的正数的碰撞,负数不碰撞。0或不同组使用categoryBits和maskBits判断
	 * @param categoryBits isSensor是false时生效。组号，必须是2的指数位值。1 2 4 8...。默认所有刚体都是1
	 * @param maskBits isSensor是false时生效。可以发生碰撞的组号，可以多个。2|4|8...。默认-1表示跟所有都碰撞
	 * @param position 生成位置
	 * @return
	 */
	private Body create(BodyType bodyType, Shape shape, short groupIndex, short categoryBits, short maskBits, Vector2 position){
		return create(bodyType, shape, false, groupIndex, categoryBits, maskBits, position);
	}
	
	/**
	 * 生成传感器刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param position 生成位置
	 * @return
	 */
	private Body createSensor(BodyType bodyType, Shape shape, Vector2 position){
		return create(bodyType, shape, true, (short)0, (short)1, (short)-1, position);
	}
	
	/**
	 * 生成刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param isSensor true 不做物理反应，只是检测碰撞
	 * @param groupIndex isSensor是false时生效。 同组的正数的碰撞,负数不碰撞。0或不同组使用categoryBits和maskBits判断
	 * @param categoryBits isSensor是false时生效。碰撞值，必须是2的指数位值。1 2 4 8...
	 * @param maskBits isSensor是false时生效。可以发生碰撞的碰撞值，可以多个。2|4|8...。-1表示所有都碰撞
	 * @param position 生成位置
	 * @return
	 */
	private Body create(BodyType bodyType, Shape shape, boolean isSensor, short groupIndex, short categoryBits, short maskBits, Vector2 position){
		
		BodyDef bodyDef = new BodyDef(); // 刚体属性
		bodyDef.type = bodyType; // 刚体类型
		bodyDef.position.set(position); // 生成位置
		
		Body body = world.createBody(bodyDef); // 生成刚体
		
		FixtureDef fixtureDef = new FixtureDef(); // 物理属性
		fixtureDef.shape = shape; // 图形
		fixtureDef.density = 1f; // 密度 非负数
		fixtureDef.friction = 0f; // 摩擦力 取值0-1
		fixtureDef.restitution = 0f; // 弹力（还原力） 取值0-1
		fixtureDef.isSensor = isSensor; // true 不做物理反应，只是检测碰撞
		
		// 碰撞过滤。isSensor是true的刚体一定要检测碰撞，不设置过滤
		if(!isSensor){
			fixtureDef.filter.groupIndex = groupIndex; //
			fixtureDef.filter.categoryBits = categoryBits; 
			fixtureDef.filter.maskBits = maskBits; 
		}
		
		body.createFixture(fixtureDef); // 生成Fixture，直接设置了不用接受返回值
		
		return body;
	}
	
	/**
	 * 销毁刚体
	 * 
	 * @param body
	 */
	public void disposeBody(Body body){
		
		if(body == null)
			return;
		
		world.destroyBody(body);
	}
	
	/**
	 * 销毁
	 */
	public void dispose(){
		
		while(!world.isLocked()){ // 物理世界锁的时候不能操作。step的时候会
			world.dispose();
			break;
		}
	}
}