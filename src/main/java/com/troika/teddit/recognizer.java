package com.troika.teddit;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class recognizer{
	private AudioFormat format;
	private DataLine.Info info;
	private TargetDataLine microphone;
	private Model model;
	private Recognizer recognizer;
	private int numBytesRead;
	private int chunkSize = 512;
	private byte[] bytes = new byte[4096];
	
	recognizer(){}
	
	void startRecognition() throws LineUnavailableException, IOException{
		LibVosk.setLogLevel(LogLevel.DEBUG);
		format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
		info = new DataLine.Info(TargetDataLine.class, format);
		microphone = (TargetDataLine) AudioSystem.getLine(info);
		model = new Model("vosk-model-small-en-us-0.15");
		microphone.open(format);
		microphone.start();
		recognizer = new Recognizer(model, 120000);
	}
	
	String getMessage(){
		numBytesRead = microphone.read(bytes, 0, chunkSize);
		if(recognizer.acceptWaveForm(bytes, numBytesRead)){
			String speech = recognizer.getResult();
			return speech.substring(14, speech.length() - 3);
		}
		return "";
	}
	
	void stopRecognition(){
		recognizer.close();
		microphone.stop();
		microphone.close();
		model.close();
	}
}
