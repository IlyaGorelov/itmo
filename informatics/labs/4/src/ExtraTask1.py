def serialize(data,indent=0):
    space = " " * indent

    if isinstance(data, dict):
        items = []
        for key, value in data.items():
            items.append(f'{space}    "{key}": {serialize(value, indent + 4)}')
        return "{\n" + ",\n".join(items) + f"\n{space}}}"
    elif isinstance(data, list):
        items = [serialize(v, indent + 4) for v in data]
        return "[ " + ", ".join(items) + " ]"
    elif isinstance(data, str):
        return f'"{data}"'
    elif isinstance(data, bool):
        return "true" if data else "false"
    elif data is None:
        return "null"
    else:
        return str(data)

from Task1 import deserialize

toml_text = open('Schedule.toml').readlines()
data = deserialize(toml_text)

result = serialize(data)

with open("Results/result_ExtraTask1.json",'w') as f:
    for l in result:
       f.write(str(l))
    
print(result)

