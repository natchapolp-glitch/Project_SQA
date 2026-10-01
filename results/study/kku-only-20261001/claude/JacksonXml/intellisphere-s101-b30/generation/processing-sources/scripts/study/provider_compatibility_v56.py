"""Dispatch the historical XML repair only for its exact historical source."""
from normalize_provider_source import sha
from provider_compatibility_v38 import repair as repair_v38
from provider_compatibility_v39 import repair as repair_v39

def repair(tests, project, tool, seed):
    if (project, tool, seed) == ('JacksonXml', 'intellisphere', 101):
        path = tests / 'com/fasterxml/jackson/dataformat/xml/deser/FromXmlParserTest.java'
        if path.exists() and sha(path) != '7ab625decbf4f864aaafd865a81683528e121baa07915555aa3a7a185800a860':
            return repair_v38(tests, project, tool, seed)
    return repair_v39(tests, project, tool, seed)
