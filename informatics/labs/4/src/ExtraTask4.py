import Task1, ExtraTask1, toml,json, ExtraTask3
import time

f = open('Schedule.toml').readlines()

start = time.time()
for i in range(100):
    data = Task1.deserialize(f)
    ExtraTask1.serialize(data)
end = time.time()
print(f"Без библиотек: {end-start:.5f}")

start = time.time()
for i in range(100):
    data = toml.load('Schedule.toml')
    json.dumps(data)
end = time.time()
print(f"Библиотека toml: {end-start:.5f}")

start = time.time()
for i in range(100):
    data = Task1.deserialize(f)
    ExtraTask3.serialize(data)
end = time.time()
print(f"Без библиотек в xml: {end-start:.5f}")