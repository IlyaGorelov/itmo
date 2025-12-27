def deserialize(toml_text: str) -> dict:
    result = {}
    current_section = result
    
    for line in toml_text:
        line2 = line.strip()
        
        if not line2 or line2[0]=='#':
            continue
        
        if line2.startswith('[') and line2.endswith(']'):
            section_name = line2[1:-1].strip()
            parts = section_name.split('.')
            
            current_section = result
            for part in parts:
                current_section = current_section.setdefault(part, {})
            continue
        
        key, value = map(str.strip, line2.split('='))
        
        current_section[key] = parse_value(value)
    
    return result 
        
def parse_value(value: str):
    if value.startswith('"') and value.endswith('"'):
        return value[1:-1]

    if value in ("true", "false"):
        return value == "true"

    if value.lstrip("+-").isdigit():
        return int(value)
    
    try:
        if "." in value:
            return float(value)
    except:
        pass

    if value.startswith("[") and value.endswith("]"):
        inner = value[1:-1].strip()
        if not inner:
            return []
        elements = split_array(inner)
        return [parse_value(v.strip()) for v in elements]

    return value     
        
def split_array(inner: str):
    result = []
    current = ""
    in_string = False

    for ch in inner:
        if ch == '"':
            in_string = not in_string
        elif ch == "," and not in_string:
            result.append(current.strip())
            current = ""
            continue
        current += ch

    if current:
        result.append(current.strip())

    return result

text = open('Schedule.toml').readlines()
result = deserialize(text)

with open("Results/result_Task1.txt", "w") as f:  
    f.writelines(str(result))    

