import os
import numpy as np
from PIL import Image

path="newSource1.png"
p=Image.open(path,"r")
p1=Image.open("normalbullet.png")
p2=Image.open("bound.png")

print("-")

size = p1.size
print(size[0])
print(size[1])

dx = p.size[0] // 16
dy = p.size[1] // 16
p1 = p1.resize((dx, dy))
p2=p2.resize((dx,dy))

p.paste(p1,(dx*8,0))
p.paste(p2,(dx*9,0))

p.show()
p.save("newSource2.png")

print(dx)
print(dy)
