package lib;

import app.ui.control.ControlPanel;
import app.ui.paint.PaintComponent;
import lib.math.Quaternion;
import lib.math._2D;

import java.awt.*;
import java.util.*;

import static app.ui.bottom.BottomPanel.statusLabel;

/**
 * K均值聚类算法类
 * @author RioJasonC
 * @version 0.1
 */
public class K_Means {
    public static final int MAX_N = 1000;

    public static final int GPM_RANDOM = 0; // 随机生成点集
    public static final int GPM_INPUT_AREA = 1; // 通过Input Area生成点集
    public static final int FCCCM_RANDOM = 0; // 随机选取初始聚类中心
    public static final int FCCCM_2PLUS = 1; // 采用K-Means++优化方法选取初始聚类中心

    public int n; // 点的数量
    public int k; // 簇的数量
    public _2D[] points; // 所有的点
    public int generatePointMod = GPM_RANDOM; // 生成点集模式
    public double clustersHashCode = 0.0;
    public Cluster[] clusters; // 所有的簇
    public static int[] clusterBelongedIds; // 点所属的簇的id
    public int firstClusterCenterChosenMod = FCCCM_RANDOM; // 第一次选择聚类中心的模式
    public static Color[] clusterColors;

    public K_Means(int n, int k) {
        this.n = n;
        this.k = k;

        points = new _2D[n];
        clusters = new Cluster[k];
        clusterBelongedIds = new int[n];
        clusterColors = new Color[k];
    }

    /**
     * 根据生成点集模式、聚类中心初始化模式生成点集、聚类中心
     */
    public void init() {
        generatePoint();
        generateClusterCenter();
        generateShape();
    }

    /**
     * 生成点集
     */
    private void generatePoint() {
        clusterBelongedIds = new int[n];
        for(int i = 0; i < n; i++) {
            clusterBelongedIds[i] = k;
        }
        switch (generatePointMod) {
            case GPM_RANDOM -> generatePoint_Random();
            case GPM_INPUT_AREA -> generatePointMod_INPUT_AREA();
        }
    }

    /**
     * 随机生成点集
     */
    private void generatePoint_Random() {
        double pad = 16;
        generatePoint_Random(new Quaternion(pad, PaintComponent.DEFAULT_WIDTH - pad, pad, PaintComponent.DEFAULT_HEIGHT - pad));
    }

    /**
     * 通过Input Area输入点集
     */
    private void generatePointMod_INPUT_AREA() {
        String inputData = ControlPanel.inputAreaTextArea.getText();
        int pointIndex = 0;
        int length = inputData.length();

        for(int i = 0; i < length; i++) {
            char c = inputData.charAt(i);

            if(c == '/' && inputData.charAt(i + 1) == '*') {
                i = i + 2;
                while(inputData.charAt(i) != '*' && inputData.charAt(i + 1) != '/') {
                    i++;
                }
                i++;
            }
            else if(c == '<') {
                int l = i;
                int r = i;
                while(inputData.charAt(r) != '>') {
                    r++;
                }

                String singlePointData = inputData.substring(l + 3, r);
                String[] singleXYData = singlePointData.split(",");
                points[pointIndex++] = new _2D(Double.parseDouble(singleXYData[0]), Double.parseDouble(singleXYData[1]));

                i = r;
            }
        }
    }

    /**
     * 随机生成点集
     * @param quaternion a ≤ x ≤ b, c ≤ y ≤ d
     */
    private void generatePoint_Random(Quaternion quaternion) {
        double minX = quaternion.a;
        double xRange = quaternion.b - minX;
        double minY = quaternion.c;
        double yRange = quaternion.d - minY;

        for(int i = 0; i < n; i++) {
            points[i] = new _2D(minX + Math.random() * xRange, minY + Math.random() * yRange);
        }
    }

    /**
     * 如果聚类中心没有被用户手动选择，则自动生成聚类中心
     */
    private void generateClusterCenter() {
        switch (firstClusterCenterChosenMod) {
            case FCCCM_RANDOM -> generateClusterCenter_Random();
            case FCCCM_2PLUS -> generateClusterCenter_2Plus();
        }
    }

    /**
     * 随机生成聚类中心
     */
    private void generateClusterCenter_Random() {
        Random random = new Random();
        HashSet<Integer> hashSet = new HashSet<>();

        while(hashSet.size() < k) {
            int temp = random.nextInt(n);
            hashSet.add(temp);
        }

        ArrayList<Integer> arrayList = new ArrayList<>(hashSet);
        Collections.sort(arrayList);
        for(int i = 0; i < k; i++) {
            clusters[i] = new Cluster(points[arrayList.get(i)]);
        }
    }

    /**
     * K-Means++优化随机生成聚类中心
     */
    private void generateClusterCenter_2Plus() {
        Random random = new Random();
        boolean[] isClusterCenter = new boolean[n]; // 是否为聚类中心
        int clusterCenterAmount = 1; // 当前聚类中心的数量
        int preClusterCenterId; // 上一步新添加的聚类中心的id
        int[] clusterCenterId = new int[k]; // value->聚类中心id
        double[] minDis = new double[n]; // 点到当前所有聚类中心的最小距离

        Arrays.fill(minDis, Double.MAX_VALUE);
        clusterCenterId[0] = random.nextInt(n);
        preClusterCenterId = clusterCenterId[0];

        while(clusterCenterAmount < k) {
            for(int i = 0; i < n; i++) {
                if(isClusterCenter[i]) {
                    continue;
                }

                _2D p = points[i];
                minDis[i] = Math.min(p.getDis(points[preClusterCenterId]), minDis[i]); // 更新最小距离
            }

            double _maxDis = 0.0;
            int newClusterCenterId = -1;

            for(int i = 0; i < n; i++) {
                if(!isClusterCenter[i] && minDis[i] > _maxDis) {
                    _maxDis = minDis[i];
                    newClusterCenterId = i;
                }
            }

            isClusterCenter[newClusterCenterId] = true;
            preClusterCenterId = newClusterCenterId;
            clusterCenterId[clusterCenterAmount++] = newClusterCenterId;
        }

        for(int i = 0; i < k; i++) {
            clusters[i] = new Cluster(points[clusterCenterId[i]]);
        }
    }

    /**
     * 生成绘图所需要的颜色数据等
     */
    private void generateShape() {
        Random random = new Random();

        clusterColors = new Color[k + 1];
        clusterColors[k] = Color.BLACK;
        for(int i = 0; i < k; i++) {
            clusterColors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
    }

    /**
     * 进行一次聚类
     */
    public boolean clusterOnce() {
        return cluster_Normal();
    }

    /**
     * 聚类直到再也无法聚类
     */
    public void cluster() {
        while(!clusterOnce()) {
            statusLabel.setText("Cluster Once Success");
        }
    }

    /**
     * 未优化的聚类算法（进行一次）
     * 同时判断是否聚类已经完全完成
     * @return boolean
     */
    private boolean cluster_Normal() {
        // 获取每个点对应最近的簇
        for(int i = 0; i < n; i++) {
            double minDis = Double.MAX_VALUE;
            int newClusterId = -1;

            for(int j = 0; j < k; j++) {
                double dis = points[i].getDis(clusters[j].centroidPos);

                if(dis < minDis) {
                    minDis = dis;
                    newClusterId = j;
                }
            }
            clusterBelongedIds[i] = newClusterId;
        }

        // 更新簇的状态
        for(int i = 0; i < k; i++) {
            clusters[i] = new Cluster(new _2D(0.0, 0.0));
        }

        for(int i = 0; i < n; i++) {
            int clusterId = clusterBelongedIds[i];

            clusters[clusterId].centroidPos = clusters[clusterId].centroidPos.add(points[i]);
            clusters[clusterId].points.add(i);
        }

        // 判断是否达到边界条件
        double tempClustersHashCode0 = 0.0;
        double tempClustersHashCode1 = 0.0;
        for(int i = 0; i < k; i++) {
            clusters[i].centroidPos = clusters[i].centroidPos.div(clusters[i].points.size());

            _2D tempPoint = clusters[i].centroidPos;
            tempClustersHashCode0 += tempPoint.x + tempPoint.y;
            tempClustersHashCode1 += Math.sqrt(tempPoint.x) + Math.sqrt(tempPoint.y);
        }

        tempClustersHashCode0 += tempClustersHashCode1;
        if(Math.abs(tempClustersHashCode0 - clustersHashCode) < 1e-5) {
            clustersHashCode = tempClustersHashCode0;
            return true;
        }

        clustersHashCode = tempClustersHashCode0;
        return false;
    }
}