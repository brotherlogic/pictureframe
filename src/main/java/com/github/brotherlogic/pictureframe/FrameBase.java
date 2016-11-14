package com.github.brotherlogic.pictureframe;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.github.brotherlogic.javaserver.JavaServer;

public abstract class FrameBase extends JavaServer {

	protected int compareFiles(File o1, File o2) {
		if (o1.lastModified() == o2.lastModified())
			return 0;
		else if (o1.lastModified() > o2.lastModified())
			return 1;
		else
			return -1;
	}

	protected Photo getLatestPhoto(String directory) {
		File[] files = new File(directory).listFiles();
		if (files != null) {
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return compareFiles(o1, o2);
				}
			});

			if (files.length > 0)
				return new Photo(files[files.length - 1]);
		}
		return null;
	}

	protected Photo getTimedLatestPhoto(String directory) {
		File[] files = new File(directory).listFiles();
		if (files != null) {
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return compareFiles(o1, o2);
				}
			});

			Calendar cal = Calendar.getInstance();
			int index = Math.abs(cal.get(Calendar.HOUR_OF_DAY) - 7) % files.length;

			if (files.length > index) {
				return new Photo(files[files.length - 1 - index]);
			}
		}
		return null;
	}

	protected String getGreeting() {
		return "Running";
	}

}
