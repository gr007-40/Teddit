package org.vosk;

import com.sun.jna.PointerType;

import java.io.IOException;

public class Recognizer extends PointerType implements AutoCloseable{
	public Recognizer(Model model, float sampleRate) throws IOException{
		super(LibVosk.vosk_recognizer_new(model, sampleRate));
		
		if(getPointer() == null){
			throw new IOException("Failed to create a recognizer");
		}
	}
	
	public Recognizer(Model model, float sampleRate, SpeakerModel spkModel){
		super(LibVosk.vosk_recognizer_new_spk(model.getPointer(), sampleRate, spkModel.getPointer()));
	}
	
	public Recognizer(Model model, float sampleRate, String grammar){
		super(LibVosk.vosk_recognizer_new_grm(model.getPointer(), sampleRate, grammar));
	}
	
	public void setMaxAlternatives(int maxAlternatives){
		LibVosk.vosk_recognizer_set_max_alternatives(getPointer(), maxAlternatives);
	}
	
	public void setWords(boolean words){
		LibVosk.vosk_recognizer_set_words(getPointer(), words);
	}
	
	public void setSpeakerModel(SpeakerModel spkModel){
		LibVosk.vosk_recognizer_set_spk_model(getPointer(), spkModel.getPointer());
	}
	
	public boolean acceptWaveForm(byte[] data, int len){
		return LibVosk.vosk_recognizer_accept_waveform(getPointer(), data, len);
	}
	
	public boolean acceptWaveForm(short[] data, int len){
		return LibVosk.vosk_recognizer_accept_waveform_s(getPointer(), data, len);
	}
	
	public boolean acceptWaveForm(float[] data, int len){
		return LibVosk.vosk_recognizer_accept_waveform_f(getPointer(), data, len);
	}
	
	public String getResult(){
		return LibVosk.vosk_recognizer_result(getPointer());
	}
	
	public String getPartialResult(){
		return LibVosk.vosk_recognizer_partial_result(getPointer());
	}
	
	public String getFinalResult(){
		return LibVosk.vosk_recognizer_final_result(getPointer());
	}
	
	public void reset(){
		LibVosk.vosk_recognizer_reset(getPointer());
	}
	
	@Override
	public void close(){
		LibVosk.vosk_recognizer_free(getPointer());
	}
}
