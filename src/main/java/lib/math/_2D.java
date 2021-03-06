package lib.math;
/**
 * 2D平面类
 * @author RioJasonC
 * @version 0.1
 */
public class _2D {
    public double x, y;

    public _2D() {

    }

    public _2D(double _x, double _y) {
        this.x = _x;
        this.y = _y;
    }

    /**
     * 与某个点对应坐标相加
     * @param p
     * @return _2D
     */
    public _2D add(_2D p) {
        return new _2D(this.x + p.x, this.y + p.y);
    }

    /**
     * 与某个点对应坐标相减
     * @param p
     * @return _2D
     */
    public _2D sub(_2D p) {
        return new _2D(this.x - p.x, this.y - p.y);
    }

    /**
     * 坐标同时除以某个数
     * @param p
     * @return _2D
     */
    public _2D div(double p) {
        return new _2D(this.x / p, this.y / p);
    }

    /**
     * 获得与某个点的距离
     * @return double
     */
    public double getDis(_2D p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    /**
     * 点乘
     */
    public double getDotMul(_2D p) {
        return this.x * p.x + this.y * p.y;
    }
}
