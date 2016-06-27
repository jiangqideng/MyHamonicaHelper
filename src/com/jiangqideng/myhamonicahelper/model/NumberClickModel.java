package com.jiangqideng.myhamonicahelper.model;

import com.jiangqideng.myhamonicahelper.uinote.MusicNoteLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-27 下午1:41:32
 * @description 点击数字，新增音符的逻辑
 */
public class NumberClickModel {
	public void setMusicNoteLayout(MusicNoteLayout musicNoteLayout,
			RelativeLayout outputLayout, RelativeLayout inputLayout) {
		int indexCursorLine = musicNoteLayout.getIndexCursorLine();
		int indexCursorRow = musicNoteLayout.getIndexCursorRow();
		int line = musicNoteLayout.getLine();
		float size = musicNoteLayout.getSize();

		TextView textView;
		for (int i = musicNoteLayout.getStandardNums().size() - 1; i >= line
				* indexCursorRow + indexCursorLine; i--) {
			for (int j = 1; j < 5; j++) {
				textView = (TextView) inputLayout.findViewById((i - 1) * 4 + j);
				if (textView.getX() >= 26 * size * (line - 1)) {
					textView.setX(textView.getX() - 26 * size * (line - 1));
					textView.setY(textView.getY() + 31 * size);
				} else {
					textView.setX(textView.getX() + 26 * size);
				}
				textView.setId(i * 4 + j);
			}
		}
		for (int i = musicNoteLayout.getStandardNums().size() - 1; i >= line
				* indexCursorRow + indexCursorLine; i--) {
			for (int j = 1; j < 5; j++) {
				textView = (TextView) outputLayout
						.findViewById((i - 1) * 4 + j);
				if (textView.getX() >= 26 * size * (line - 1)) {
					textView.setX(textView.getX() - 26 * size * (line - 1));
					textView.setY(textView.getY() + 31 * size);
				} else {
					textView.setX(textView.getX() + 26 * size);
				}
				textView.setId(i * 4 + j);
			}
		}
	}

}
