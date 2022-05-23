package lib.K_means;

import lib.math._2D;

import java.util.*;

/**
 * K均值聚类算法类
 * @author RioJasonC
 * @version 0.1
 */
public class K_Means {
    public static final int MAX_CLUSTER_CENTER_AMOUNT = 100000;

    public static final int FCCCM_OPTIONAL = 0; // 自选聚类中心
    public static final int FCCCM_RANDOM = 1; // 随机选取初始聚类中心
    public static final int FCCCM_2PLUS = 2; // 采用K-Means++优化方法选取初始聚类中心

    public static final int CM_NORMAL = 0; // 基础的聚类模式
    public static final int CM_ELKAN = 1; // Elken K-Means优化后的聚类模式

    public _2D[] point;
    public int n = 100; // 点的数量
    public int[] groupId; // 对应点属于的聚类中心的id
    public int k = 10; // 聚类中心数量
    public int[] clusterCenterId; // 聚类中心id
    public int firstClusterCenterChosenMod = 1; // 第一次选择聚类中心的模式
    public int clusterMod = 0; // 聚类模式

    public void init() {
        point = new _2D[n];
        groupId = new int[n];

        for(int i = 0; i < n; i++) {
            groupId[i] = -1;
        }

        generateClusterCenter();
    }

    /**
     * 如果聚类中心没有被用户手动选择，则自动生成聚类中心
     */
    private void generateClusterCenter() {
        switch (firstClusterCenterChosenMod) {
            case FCCCM_OPTIONAL: break;
            case FCCCM_RANDOM: generateClusterCenter_Random(); break;
            case FCCCM_2PLUS: generateClusterCenter_2Plus(); break;
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
            clusterCenterId[i] = arrayList.get(i);
        }
    }

    /**
     * K-Means++优化随机生成聚类中心
     */
    private void generateClusterCenter_2Plus() {
        Random random = new Random();
        boolean[] isClusterCenter = new boolean[n]; // 是否为聚类中心
        int _clusterCenterAmount = 1; // 当前聚类中心的数量
        int preClusterCenterId; // 上一步新添加的聚类中心的id
        int[] _clusterCenterId = new int[k]; // value->聚类中心id
        double[] minDis = new double[k]; // 点到当前所有聚类中心的最小距离
        Arrays.fill(minDis, Double.MAX_VALUE);
        _clusterCenterId[0] = random.nextInt(n);
        preClusterCenterId = _clusterCenterId[0];

        while(_clusterCenterAmount < k) {
            for(int i = 0; i < n; i++) {
                if(isClusterCenter[i]) {
                    continue;
                }

                _2D p = point[i];
                minDis[i] = Math.min(p.getDis_Square(point[preClusterCenterId]), minDis[i]); // 更新最小距离
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
            _clusterCenterId[_clusterCenterAmount++] = newClusterCenterId;
        }

        for(int i = 0; i < k; i++) {
            clusterCenterId[i] = _clusterCenterId[i];
        }
    }

    /**
     * 进行一次聚类
     */
    public void clusterOnce() {
        switch (clusterMod) {
            case CM_NORMAL -> cluster_Normal();
            case CM_ELKAN -> cluster_Elkan();
        }
    }

    /**
     * 聚类直到再也无法聚类
     */
    public void cluster() {

    }

    /**
     * 未优化的聚类算法（进行一次）
     */
    private void cluster_Normal() {

    }

    /**
     * Elkan K-Means优化的聚类算法（进行一次）
     */
    private void cluster_Elkan() {

    }
}
