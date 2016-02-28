/**
 * Copyright (c) 2015,2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Converting Text data feed into CSV UTF-8
 * I have extracted product datafeed from my product suppliers FTP server. The problem is that the datafeed is in a textformat which I can't upload in my E-commerce store. The text has to be converted into a CSV UTF-8 file.
 */
File file = new File( "export2.csv" )
File output = new File( "export3_2.csv" )

int id = 0;

String previousLine = "";

file.eachLine { line ->
	id++;

if (line.startsWith("\"")) {
		line = line.substring(1);
	}

if (line.endsWith("\"")) {
		line = line.substring(0, line.length()-1);
	}

	if (line.endsWith("\n")) {
		line = line.substring(0, line.length()-1);
	}

if (id == 1) {
	output << "$line\n"
	return;
}
print "line $id  "

if (line.length() >= 14) {

	String str = line.substring(0,14)

	boolean newLine = str.contains(",");

	if (str.contains("<p>")) {
		newLine = false;
	}
	if (str.contains("</p>")) {
		newLine = false;
	}
	if (str.contains("<p")) {
		newLine = false;
	}
	if (str.contains("<div>")) {
		newLine = false;
	}
	if (str.contains("</div>")) {
		newLine = false;
	}

	if (!newLine) {
		previousLine += line;
	}

	if (newLine) {
		if (!previousLine.trim().isEmpty()) {
			output << "$previousLine\n"
		}
		previousLine = line;

	}
}


}

output << "\n$previousLine"


println "Converting ended"
