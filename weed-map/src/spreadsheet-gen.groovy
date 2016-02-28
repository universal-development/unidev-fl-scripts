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
 * Data Scrub from a website and turned into an Excel Spreadsheet
 *
 * I need someone to go onto www.weedmaps.com and pull all the California dispensary and delivery services into a spreadsheet.
 *
 * https://www.upwork.com/jobs/Data-Scrub-from-website-and-turned-into-Excel-Spreadsheet_~019a3f965e79dbc014
 */
@Grab(group = 'net.sf.opencsv', module = 'opencsv', version = '2.3')
import au.com.bytecode.opencsv.*

import groovy.json.JsonSlurper



def inputFile = new File("input.json")
def inputJSON = new JsonSlurper().parseText(inputFile.text)

//println inputJSON._mapConfig.listings[0].name

FileWriter fileWriter = new FileWriter("output.csv");

CSVWriter writer = new CSVWriter(fileWriter);

writer.writeNext((String[]) ['Name', "Phone", "Email", "Address", "City", "State", "Licence",])


inputJSON._mapConfig.listings.each {
    writer.writeNext((String[]) [ it.name , it.phone_number, it.email, it.address, it.city, it.state, it.license_type])



}

fileWriter.flush()
writer.close()