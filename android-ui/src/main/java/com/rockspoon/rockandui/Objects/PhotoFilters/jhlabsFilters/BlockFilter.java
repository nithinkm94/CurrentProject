/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.rockspoon.rockandui.Objects.PhotoFilters.JhlabsFilters;

import com.rockspoon.rockandui.utils.PixelUtil;

/**
 * A Filter to pixellate images.
 */
public class BlockFilter {

  private int blockSize = 2;

  /**
   * Construct a BlockFilter.
   */
  public BlockFilter() {
  }

  /**
   * Construct a BlockFilter.
   *
   * @param blockSize the number of pixels along each block edge
   */
  public BlockFilter(int blockSize) {
    this.blockSize = blockSize;
  }

  /**
   * Get the pixel block size.
   *
   * @return the number of pixels along each block edge
   * @see #setBlockSize
   */
  public int getBlockSize() {
    return blockSize;
  }

  /**
   * Set the pixel block size.
   *
   * @param blockSize the number of pixels along each block edge
   * @min-value 1
   * @max-value 100+
   * @see #getBlockSize
   */
  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }

  public int[] filter(int[] src, int W, int H) {
    int[] dst = new int[W * H];
    int[] pixels = new int[blockSize * blockSize];

    for (int y = 0; y < H; y += blockSize) {
      for (int x = 0; x < W; x += blockSize) {
        int w = Math.min(blockSize, W - x);
        int h = Math.min(blockSize, H - y);
        int t = w * h;

        PixelUtil.getRGB(src, x, y, w, h, W, pixels);

        int r = 0, g = 0, b = 0;
        int argb;
        int i = 0;

        for (int by = 0; by < h; by++) {
          for (int bx = 0; bx < w; bx++) {
            argb = pixels[i];
            r += (argb >> 16) & 0xff;
            g += (argb >> 8) & 0xff;
            b += argb & 0xff;
            i++;
          }
        }

        argb = ((r / t) << 16) | ((g / t) << 8) | (b / t);
        i = 0;

        for (int by = 0; by < h; by++) {
          for (int bx = 0; bx < w; bx++) {
            pixels[i] = (pixels[i] & 0xff000000) | argb;
            i++;
          }
        }

        PixelUtil.setRGB(dst, x, y, w, h, W, pixels);
      }
    }
    return dst;
  }

  public String toString() {
    return "Pixellate/Mosaic...";
  }
}

