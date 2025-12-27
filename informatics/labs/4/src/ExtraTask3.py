def serialize(data, root_tag="data", indent=0):
    xml_lines = ""
    space = " " * indent

    if isinstance(data, dict):
        xml_lines+=f"{space}<{root_tag}>\n"
        for key, value in data.items():
            xml_lines+=serialize(value, root_tag=key, indent=indent + 4)
        xml_lines+=f"{space}</{root_tag}>\n"
    elif isinstance(data, list):
        for item in data:
            xml_lines+=serialize(item, root_tag=root_tag, indent=indent)
    else:
        xml_lines+=f"{space}<{root_tag}>{replace1(str(data))}</{root_tag}>\n"
        

    return xml_lines

def replace1(data: str):
    return data.replace('>', '&gt').replace('<', '&lt')

from Task1 import deserialize

toml_text = open('Schedule.toml').readlines()
data = deserialize(toml_text)

result = serialize(data)

with open("Results/result_ExtraTask3.xml",'w') as f:
    for l in result:
       f.write(str(l))
    

