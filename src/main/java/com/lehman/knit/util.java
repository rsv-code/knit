/*
 * Copyright 2020 Austin Lehman (austin@rosevillecode.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.lehman.knit;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class util {
    public static String join(String delim, List<String> arr) {
        String ret = "";
        for (int i = 0; i < arr.size(); i++) {
            if (i > 0) ret += delim;
            ret += arr.get(i);
        }
        return ret;
    }

    public static ArrayList<String> fromArray(String[] arr) {
        ArrayList<String> ret = new ArrayList<String>();
        for (String str : arr) {
            ret.add(str);
        }
        return ret;
    }

    /**
	 * Reads a file with the provided file name and returns it as
	 * a String.
	 * @param FileName is a String with the file name to read.
	 * @return A String with the file contents.
	 * @throws IOException on IO exception.
	 */
	public static String read(String FileName) throws IOException
	{
		FileReader fr = null;
		try {
			fr = new FileReader(FileName);

			int len = -1;
			char[] buff = new char[4096];
			final StringBuffer buffer = new StringBuffer();
			while ((len = fr.read(buff)) > 0) { buffer.append(buff, 0, len); }

	        return buffer.toString();
	    } catch (IOException e) {
			throw e;
		} finally {
			if(fr != null) {
		        try { fr.close(); }
		        catch (IOException e) { throw e; }
			}
	    }
	}
}
