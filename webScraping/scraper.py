import urllib
import re

url = ["http://www.uah.edu/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX"]

regex = '<TITLE>(.+?)</TITLE>'
pattern = re.compile(regex)

htmlfile = urllib.urlopen(url[0])
htmltext = htmlfile.read()
title = re.findall(pattern, htmltext)
print title
# print htmltext