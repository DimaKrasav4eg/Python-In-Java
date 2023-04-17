import torch
import numpy as np
import matplotlib.pyplot as plt

import torchvision.transforms.functional as F
from torchvision.utils import draw_bounding_boxes

from torchvision.models.detection import fasterrcnn_resnet50_fpn, FasterRCNN_ResNet50_FPN_Weights

from torchvision.utils import make_grid
from torchvision.io import read_image

from pathlib import Path
from PIL import Image

import cv2

import json
import sys

# class for convert dict with items unusual type
class NpEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.integer):
            return int(obj)
        if isinstance(obj, np.floating):
            return float(obj)
        if isinstance(obj, np.ndarray):
            return obj.tolist()
        if isinstance(obj, torch.Tensor):
            return obj.tolist()
        return json.JSONEncoder.default(self, obj)


weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
transforms = weights.transforms()

image_data = sys.stdin.buffer.read()
image = torch.from_numpy(cv2.imdecode(np.frombuffer(image_data, np.uint8), cv2.IMREAD_COLOR))
print(type(image))
image = image.transpose(0,2)
print(image.shape, '1')
dog_list = [image]

images = [transforms(d) for d in dog_list]

model = fasterrcnn_resnet50_fpn(weights=weights, progress=False)
model = model.eval()
# print(images)
output = model(images)

# sending json result
json.dump(output, sys.stdout, cls=NpEncoder)
sys.stdout.flush()

with open('src/main/results/result.json', 'w') as fp:
    json.dump(output, fp, cls=NpEncoder)