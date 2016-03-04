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
 * I'm looking to scrape certain fields from the whois results for a list of at least 1000 domain names.
 Example result: https://who.godaddy.com/whoisstd.aspx?domain=sample.tv
 I would like to capture in Excel:
 - Creation Date
 - Registrar
 - Registrant Country
 - Registrant Email

 This could be a one-time project or a tool built.

 https://www.upwork.com/applications/324312490
 */


@Grab(group = 'net.sf.opencsv', module = 'opencsv', version = '2.3')
@Grab(group='commons-net', module='commons-net', version='3.4')
@Grab(group='org.apache.commons', module='commons-lang3', version='3.4')

import au.com.bytecode.opencsv.*
import org.apache.commons.lang3.StringUtils
import org.apache.commons.net.whois.WhoisClient

Map<String, String> whoisServersByZone = new HashMap<>();

new File("whois_servers.txt").eachLine { line ->
    String[] split = StringUtils.split(line, " ");

    whoisServersByZone.put(split[0], split[1]);
};

println "Loaded zones $whoisServersByZone"

FileWriter fileWriter = new FileWriter("output.csv");

CSVWriter writer = new CSVWriter(fileWriter);

writer.writeNext((String[]) ["Domain", "creationDate", "registrar", "registrantCountry", "registrantEmail"]);


new File("domains.txt").eachLine { domain ->

    //String domain = "sample.tv";
    //domain = "twitch.tv";

    String[] split = StringUtils.split(domain, ".");
    String rootZone = split[split.size()-1];
    String firstWhois = whoisServersByZone.get(rootZone);

    println "Domain $domain Zone $rootZone  whois $firstWhois"

    WhoisClient whois = new WhoisClient();
    whois.connect(firstWhois);
    String data = whois.query("=" + domain)
    whois.disconnect();

    String whoisServer = StringUtils.substringBetween(data, "WHOIS Server: ", "\n");
    println "Domain $domain, second whois server $whoisServer"
    if (!StringUtils.isBlank(whoisServer)) {
        whois = new WhoisClient();
        whois.connect(whoisServer.trim());
        data = whois.query("=" + domain)
        whois.disconnect();

    }

    //println "$data"

    String creationDate = StringUtils.substringBetween(data, "Creation Date: ", "\n")
    String registrar = StringUtils.substringBetween(data, "Registrar: ", "\n")
    String registrantCountry = StringUtils.substringBetween(data, "Registrant Country: ", "\n")
    String registrantEmail = StringUtils.substringBetween(data, "Registrant Email: ", "\n")

    println "$creationDate $registrar $registrantCountry $registrantEmail"
    writer.writeNext((String[]) [ domain , creationDate , registrar, registrantCountry, registrantEmail])

    //System.exit(-1)

}

writer.close();

println "Done..."
