#502362 - polars
import polars as pl

df = pl.read_excel('ЛР5.xlsx', has_header=False,columns=[x for x in range(0,27)],drop_empty_rows=True)

df = df.drop([f"column_{x}" for x in range(6,12)])

#12-27
selected = df.select([pl.col(f"column_{x}") for x in range(12,28)])
dicts = selected.to_dict()

results = ['']*len(dicts["column_12"])
for i in range(12,28):
    for x in range(len(dicts["column_12"])):
        results[x]+=str(dicts[f"column_{i}"][x])

for i in range(3): results[i]=None

df = df.with_columns(pl.Series("bin",results))

df = df.drop([f"column_{x}" for x in range(12,28)]+["column_4"])

df.head(15).write_excel("result.xlsx")
print(df.head(15))