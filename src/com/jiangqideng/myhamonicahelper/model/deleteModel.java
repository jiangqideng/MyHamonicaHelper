package com.jiangqideng.myhamonicahelper.model;

import java.util.ArrayList;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangqideng.myhamonicahelper.ui.MainActivity;
import com.jiangqideng.myhamonicahelper.uinote.MusicNoteLayout;
import com.jiangqideng.myhamonicahelper.utils.SizeSwitch;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-27 下午1:40:47
 * @description 删除音符时的逻辑，使用setMusicNoteLayout
 */
public class deleteModel {

	public void setMusicNoteLayout(MusicNoteLayout musicNoteLayout,
			RelativeLayout outputLayout, RelativeLayout inputLayout,
			MainActivity activity) {
		int indexCursorLine = musicNoteLayout.getIndexCursorLine();
		int indexCursorRow = musicNoteLayout.getIndexCursorRow();
		int line = musicNoteLayout.getLine();
		float size = musicNoteLayout.getSize();

		if (!(indexCursorLine == 0 && indexCursorRow == 0)) {

			ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();

			standardNums.remove(line * indexCursorRow + indexCursorLine - 1);
			musicNoteLayout.setStandardNums(standardNums);
			if (indexCursorLine == 0) {
				musicNoteLayout.setIndexCursorLine(line - 1);
				musicNoteLayout.setIndexCursorRow(indexCursorRow - 1);
				musicNoteLayout.setX((line - 1) * 26 * size
						+ SizeSwitch.dip2px(activity, 1));
				musicNoteLayout.setY(musicNoteLayout.getY() - 31 * size);
			} else {
				musicNoteLayout.setIndexCursorLine(indexCursorLine - 1);
				musicNoteLayout.setX(musicNoteLayout.getX() - 26 * size);
			}
			musicNoteLayout.drawCursor();

			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 1));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 2));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 3));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 4));

			outputLayout.removeView(outputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 1));
			outputLayout.removeView(outputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 2));
			outputLayout.removeView(outputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 3));
			outputLayout.removeView(outputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 4 + 4));

			TextView textView;
			for (int i = line * indexCursorRow + indexCursorLine - 1; i < standardNums
					.size(); i++) {
				for (int j = 1; j < 5; j++) {
					textView = (TextView) inputLayout.findViewById((i + 1) * 4
							+ j);
					if (textView.getX() <= 26 * size) {
						textView.setX((line - 1) * 26 * size + textView.getX());
						textView.setY(textView.getY() - 31 * size);
					} else {
						textView.setX(textView.getX() - 26 * size);
					}
					textView.setId(i * 4 + j);
				}
			}
			for (int i = line * indexCursorRow + indexCursorLine - 1; i < standardNums
					.size(); i++) {
				for (int j = 1; j < 5; j++) {
					textView = (TextView) outputLayout.findViewById((i + 1) * 4
							+ j);
					if (textView.getX() <= 26 * size) {
						textView.setX((line - 1) * 26 * size + textView.getX());
						textView.setY(textView.getY() - 31 * size);
					} else {
						textView.setX(textView.getX() - 26 * size);
					}
					textView.setId(i * 4 + j);
				}
			}
		}
	}

}
