package com.jiangqideng.myhamonicahelper.view;

public class MusicNote {
	public int standardNum;
	public boolean isSpace=false;
	public int [] bigMusicNum = {0,2,4,5,7,9,11};//全全半全全全半  这里数字以半音为一个单位,数组里是相差
	public String bigMusicStrings[]	= new String[] {
			"1","1","2","3","3","4","4","5","6","6","7","7"
			};
	public String offsetStrings[]	= new String[] {
			" ","#"," ","b"," "," ","#"," ","b"," ","b"," "
			};
	public MusicNote(int inputBigMusicIndex, int num, int offset, int pitch) {
	//inputBigMusicIndex为该大调在数组中的位置(0-11) 	num为简谱的数字(1-7)
	//offset为-1 0 1 代表升降调       pitch为-1 0 1 代表低音 中音 高音
		if (num == 0) {
			isSpace = true;
			standardNum=-100;
		}else {
			this.standardNum=inputBigMusicIndex + bigMusicNum[num-1] + offset + 12*pitch;
		}
	//得出的标准数的范围为：-13  ---    35
	}//2+4   0+6
	public MusicNote(int standardNum) {
		this.standardNum=standardNum;
		if (standardNum == -100) {
			isSpace = true;
		}
	}
	public String getPitchUp(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			return standardNum-outputBigMusicIndex<0 ? " " :standardNum-outputBigMusicIndex<12 ? " " : "●";
		}
	}
	public String getPitchDown(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			return standardNum-outputBigMusicIndex<0 ? "●" :standardNum-outputBigMusicIndex<12 ? " " : " ";
		}
	}
	public int getPitch(int outputBigMusicIndex){
		if (isSpace) {
			return 0;
		}else {
			return standardNum-outputBigMusicIndex<0 ? -1 :standardNum-outputBigMusicIndex<12 ? 0 : 1;
		}
	}
	public String getMusicString(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			int num = standardNum-outputBigMusicIndex-12*this.getPitch(outputBigMusicIndex);
			if (num<0 || num>11) {
				return "?";
			}else {
				return bigMusicStrings[num];
			}
		}
	}	
	public String getoffsetString(int outputBigMusicIndex){
		if (isSpace) {
			return " ";
		}else {
			int num = standardNum-outputBigMusicIndex-12*this.getPitch(outputBigMusicIndex);
			if (num<0 || num>11) {
				return " ";
			}else {
				return offsetStrings[num];
			}
		}
	}	
	public int getStandardNum(){
			return standardNum;
	}
}