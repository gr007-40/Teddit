package org.vosk;

import com.sun.jna.PointerType;

import java.io.IOException;

public class Model extends PointerType implements AutoCloseable{
	public Model(){
	}
	
	public Model(String path) throws IOException{
		super(LibVosk.vosk_model_new(path));
		
		if(getPointer() == null){
			throw new IOException("Failed to create a model");
		}
	}
	
	@Override
	public void close(){
		LibVosk.vosk_model_free(getPointer());
	}
}
