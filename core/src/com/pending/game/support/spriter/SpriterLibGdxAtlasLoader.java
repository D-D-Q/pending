package com.pending.game.support.spriter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.FileReference;
import com.brashmonkey.spriter.Loader;

/**
 * 载入TextureAtlas格式的Spriter资源图
 * 
 * @date 2017年3月4日 下午9:02:30
 */
public class SpriterLibGdxAtlasLoader extends Loader<Sprite>{
	
	private TextureAtlas atlas;

	public SpriterLibGdxAtlasLoader(Data data, FileHandle atlas, String indexPrefix) {
		super(data);
		this.atlas = new TextureAtlas(atlas);
		Array<AtlasRegion> array = this.atlas.getRegions();
		for(int i = 0; i < array.size; i++){
			AtlasRegion region = array.get(i);
			if(region.index != -1) region.name = region.name+indexPrefix+region.index;
		}
	}
	
	public SpriterLibGdxAtlasLoader(Data data, FileHandle atlas){
		this(data, atlas, "_");
	}

	@Override
	protected Sprite loadResource(FileReference ref) {
		return this.atlas.createSprite((data.getFile(ref).name).replace(".png",""));
	}
	
	@Override
	public void dispose(){
		this.atlas.dispose();
	}
}