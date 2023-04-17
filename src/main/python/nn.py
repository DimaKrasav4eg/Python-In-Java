import numpy as np
import os
import torch
from tensorflow.keras.layers import BatchNormalization
import time
import sys
from random import random
from aiogram import Dispatcher, types
from aiogram.dispatcher.filters import Text
import os
import cv2
import time
from datetime import datetime, timedelta
from pixellib.torchbackend.instance import instanceSegmentation

import pixellib
from pixellib.semantic import semantic_segmentation
import argparse
import glob
# from keras.layers.normalization.batch_normalization import BatchNormalization

import json

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

# image reception
image_data = sys.stdin.buffer.read()
image = cv2.imdecode(np.frombuffer(image_data, np.uint8), cv2.IMREAD_COLOR)

# dowload nn model
ins = instanceSegmentation()
ins.load_model("src/main/python/pointrend_resnet50.pkl")



# image analysis result
results, output = ins.segmentLoadedImage(image, show_bboxes=True, output_image_name="src/main/results/result.jpg")
del results["masks"]

# sending json result
json.dump(output, sys.stdout, cls=NpEncoder)
sys.stdout.flush()

with open('src/main/results/result-v1.json', 'w') as fp:
    json.dump(results, fp, cls=NpEncoder)