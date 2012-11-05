/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */
package org.apache.hadoop.fs.http.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Streams {
	
	public static final int EIGHT_K		= 8192;
	public static final int	TWELVE_K	= 12288;
	
	/**
	 * Reads the contents of {@code is} fully
	 * 
	 * @param is
	 * 			the {@link InputStream}
	 * @return the textual contents of {@code is}
	 * @throws IOException
	 */
	public static String toString(InputStream is) throws IOException {
		return toStringBuilder(is).toString();
	}
	
	/**
	 * Reads the contents of {@code is} fully
	 * 
	 * @param is
	 * 			the {@link InputStream}
	 * @return a {@link StringBuilder} containing the contents of {@code is}
	 * @throws IOException
	 */
	public static StringBuilder toStringBuilder(InputStream is) throws IOException {
		String line = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(is));

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		finally {
			Closeables.closeQuietly(reader);
			Closeables.closeQuietly(is);
		}

		return sb;
	}

	/**
	 * 
	 * @param instream
	 * 			the {@link InputStream} 
	 * @param outstream
	 * 			the {@link OutputStream}
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream instream, OutputStream outstream) throws IOException {
		int n = 0;
		long count = 0L;
		byte[] buffer = new byte[TWELVE_K];
		
		try {
			while(-1 != (n = instream.read(buffer))) {
				outstream.write(buffer, 0, n);
				count += n;
				outstream.flush();
			}
	
			outstream.flush();
		}
		finally {
			Closeables.closeQuietly(instream);
			Closeables.closeQuietly(outstream);
		}
		
		return count;
	}
}
