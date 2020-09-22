/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Okan
 */
import java.io.Serializable;

/**
 * A tile represents a rectangular part of the world map. All tiles can be identified by their X and Y number together
 * with their zoom level. The actual area that a tile covers on a map depends on the underlying map projection.
 */
public class Tile implements Serializable {
        /**
         * Width and height of a map tile in pixel.
         */
        public static final int TILE_SIZE = 150;

        private static final long serialVersionUID = 1L;

        /**
         * The X number of this tile.
         */
        public final long tileX;

        /**
         * The Y number of this tile.
         */
        public final long tileY;

        /**
         * The zoom level of this tile.
         */
        public final byte zoomLevel;

        /**
         * @param tileX
         *            the X number of the tile.
         * @param tileY
         *            the Y number of the tile.
         * @param zoomLevel
         *            the zoom level of the tile.
         */
        public Tile(long tileX, long tileY, byte zoomLevel) {
                this.tileX = tileX;
                this.tileY = tileY;
                this.zoomLevel = zoomLevel;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                } else if (!(obj instanceof Tile)) {
                        return false;
                }
                Tile other = (Tile) obj;
                if (this.tileX != other.tileX) {
                        return false;
                } else if (this.tileY != other.tileY) {
                        return false;
                } else if (this.zoomLevel != other.zoomLevel) {
                        return false;
                }
                return true;
        }

        /**
         * @return the pixel X coordinate of the upper left corner of this tile.
         */
        public long getPixelX() {
                return this.tileX * TILE_SIZE;
        }

        /**
         * @return the pixel Y coordinate of the upper left corner of this tile.
         */
        public long getPixelY() {
                return this.tileY * TILE_SIZE;
        }

        @Override
        public int hashCode() {
                int result = 7;
                result = 31 * result + (int) (this.tileX ^ (this.tileX >>> 32));
                result = 31 * result + (int) (this.tileY ^ (this.tileY >>> 32));
                result = 31 * result + this.zoomLevel;
                return result;
        }

        @Override
        public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("tileX=");
                stringBuilder.append(this.tileX);
                stringBuilder.append(", tileY=");
                stringBuilder.append(this.tileY);
                stringBuilder.append(", zoomLevel=");
                stringBuilder.append(this.zoomLevel);
                return stringBuilder.toString();
        }
}
