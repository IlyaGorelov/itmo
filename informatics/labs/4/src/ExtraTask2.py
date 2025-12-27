import toml, json

data = toml.load('Schedule.toml')
with open("Results/result1_ExtraTask2.txt", "w") as f:  
    f.writelines(str(data)) 

toml_str = json.dumps(data, ensure_ascii=False, indent=4)
with open("Results/result2_ExtraTask2.json",'w') as f:
    for l in toml_str:
       f.write(str(l))